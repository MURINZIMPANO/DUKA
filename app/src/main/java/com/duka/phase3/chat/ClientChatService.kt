package com.duka.phase3.chat

import com.duka.app.data.local.dao.ClientChatMessageDao
import com.duka.app.data.local.entity.ClientChatMessage
import com.duka.phase3.sync.SupabaseConfig
import com.duka.phase3.sync.SupabaseRestClient
import com.duka.phase3.sync.longOrNull
import com.duka.phase3.sync.str
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.jsonObject
import java.security.MessageDigest

/**
 * Phase 3 — Owner ↔ Client chat, cross-device via Supabase.
 *
 * EXTENDS, does not rebuild: reuses the V1/V2 ChatMessage *shape* (senderRole,
 * senderLabel, text, timestamp) plus the spec's `participantType` discriminator.
 * Stored in the NEW client_chat_messages table — the existing owner↔employee
 * chat_messages table is untouched.
 *
 * REALTIME: Supabase's realtime channel needs websockets; to keep zero new
 * dependencies we poll at a short interval while the conversation is open and
 * refresh on every send. DELIBERATE CHOICE: 3s polling is indistinguishable from
 * realtime for chat UX at this scale; swap in supabase-kt's realtime channel later
 * without changing call sites (observeConversation + sendMessage stay the API).
 *
 * Offline behaviour: sends are marked pendingPush locally and retried each poll
 * cycle; failures surface via [sendState] so the UI can show a visible,
 * retriable "not yet delivered" state — never silent.
 */
class ClientChatService(
    private val dao: ClientChatMessageDao,
    private val client: SupabaseRestClient = SupabaseRestClient()
) {
    data class SendState(
        val inProgress: Boolean = false,
        val lastError: String? = null,
        val configured: Boolean = SupabaseConfig.IS_CONFIGURED
    )

    private val _sendState = MutableStateFlow(SendState())
    val sendState: StateFlow<SendState> = _sendState

    private val json = Json { ignoreUnknownKeys = true }
    private var pollJob: Job? = null

    /** Deterministic conversation id for an owner↔shop↔client triple. */
    fun conversationId(shopRemoteId: String, clientUserId: Long): String {
        val raw = "duka-conv-$shopRemoteId-$clientUserId"
        val bytes = MessageDigest.getInstance("MD5").digest(raw.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun observeConversation(conversationId: String): Flow<List<ClientChatMessage>> =
        dao.observeConversation(conversationId)

    /**
     * Send a message: insert locally immediately (local-first), then push.
     * On success the row is confirmed with its server id; on failure it stays
     * pending and the error is visible in [sendState].
     */
    suspend fun sendMessage(
        conversationId: String,
        shopRemoteId: String,
        clientUserId: Long,
        senderRole: String,          // "owner" | "client"
        senderLabel: String,
        text: String,
        timestamp: Long = System.currentTimeMillis()
    ) {
        val local = ClientChatMessage(
            remoteId = "",
            conversationId = conversationId,
            shopId = shopRemoteId,
            clientUserId = clientUserId,
            senderRole = senderRole,
            participantType = "client",   // this conversation type; employee chats stay in the old table
            senderLabel = senderLabel,
            text = text,
            timestamp = timestamp,
            pendingPush = true
        )
        dao.upsert(local)

        if (!SupabaseConfig.IS_CONFIGURED) {
            _sendState.value = SendState(
                lastError = "Backend not configured — message saved on this device only",
                configured = false
            )
            return
        }

        pushOne(local, shopRemoteId, clientUserId)
    }

    /**
     * Poll for new messages for one conversation while its screen is open and
     * retry pending sends. Returns a cancellable Job; call from a LaunchedEffect.
     */
    fun startPolling(
        scope: CoroutineScope,
        conversationId: String,
        shopRemoteId: String,
        clientUserId: Long
    ): Job {
        pollJob?.cancel()
        pollJob = scope.launch {
            while (isActive) {
                pullMessages(conversationId, shopRemoteId)
                retryPending(conversationId, shopRemoteId, clientUserId)
                delay(3_000)
            }
        }
        return pollJob!!
    }

    fun stopPolling() {
        pollJob?.cancel()
        pollJob = null
    }

    /** PULL — merge remote messages into the local cache (idempotent upsert). */
    suspend fun pullMessages(conversationId: String, shopRemoteId: String) {
        if (!SupabaseConfig.IS_CONFIGURED) return
        val res = client.get(
            table = "messages",
            filters = mapOf(
                "conversation_id" to "eq.$conversationId",
                "shop_id" to "eq.$shopRemoteId"
            ),
            order = "timestamp_millis.asc",
            limit = 200
        )
        if (res is SupabaseRestClient.Result.Success) {
            val rows = client.parseArray(res.body).mapNotNull { el ->
                val o = try { el.jsonObject } catch (_: Exception) { return@mapNotNull null }
                try {
                    ClientChatMessage(
                        remoteId = o.str("id"),
                        conversationId = o.str("conversation_id"),
                        shopId = o.str("shop_id"),
                        clientUserId = o.longOrNull("client_user_id"),
                        senderRole = o.str("sender_role"),
                        participantType = "client",
                        senderLabel = o.str("sender_label"),
                        text = o.str("text"),
                        timestamp = o.longOrNull("timestamp_millis"),
                        pendingPush = false
                    )
                } catch (_: Exception) {
                    null // malformed row — skip rather than crash the conversation
                }
            }
            if (rows.isNotEmpty()) dao.upsertAll(rows)
        }
        // Pull failures are intentionally non-fatal: the conversation still shows
        // local messages and the next poll retries automatically.
    }

    /** Retry any locally-pending sends (e.g. after connectivity returns). */
    private suspend fun retryPending(conversationId: String, shopRemoteId: String, clientUserId: Long) {
        val pending = dao.pendingFor(conversationId)
        pending.forEach { msg ->
            pushOne(msg, shopRemoteId, clientUserId)
        }
    }

    private suspend fun pushOne(msg: ClientChatMessage, shopRemoteId: String, clientUserId: Long) {
        _sendState.value = _sendState.value.copy(inProgress = true, configured = true)
        val payload = buildJsonObject {
            put("id", remoteUuidFor(msg.conversationId, msg.timestamp))
            put("conversation_id", msg.conversationId)
            put("shop_id", shopRemoteId)
            put("client_user_id", clientUserId)
            put("sender_role", msg.senderRole)
            put("sender_label", msg.senderLabel)
            put("text", msg.text)
            put("timestamp_millis", msg.timestamp)
        }
        val res = client.post("messages", Json.encodeToString(JsonObject.serializer(), payload))
        when (res) {
            is SupabaseRestClient.Result.Success -> {
                dao.confirmPush(msg.timestamp, remoteUuidFor(msg.conversationId, msg.timestamp))
                _sendState.value = SendState(configured = true)
            }
            is SupabaseRestClient.Result.Failure -> {
                _sendState.value = SendState(
                    lastError = "Message not yet delivered: ${res.message}",
                    configured = true
                )
            }
        }
    }

    /** Deterministic UUID for a message so repeated pushes upsert, not duplicate. */
    private fun remoteUuidFor(conversationId: String, timestamp: Long): String {
        val bytes = MessageDigest.getInstance("MD5")
            .digest("duka-msg-$conversationId-$timestamp".toByteArray())
        val s = bytes.joinToString("") { "%02x".format(it) }
        return "${s.substring(0, 8)}-${s.substring(8, 12)}-${s.substring(12, 16)}-${s.substring(16, 20)}-${s.substring(20, 32)}"
    }
}
