package com.duka.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Phase 3 — Explore + shared backend sync.
 *
 * SCOPE-GUARD: This is a NEW read-mostly cache table for the Explore feature.
 * It does NOT replace the V1/V2 `businesses` table, which remains each shop's
 * local source of truth for its own profile. Explore caches PUBLIC profiles of
 * OTHER shops pulled down from Supabase. Writes only happen in SyncService.
 *
 * One row per remote shop. `remoteId` is the Supabase `shops.id` (UUID string).
 * A shop's own row (when it syncs up) uses its own UUID — the local business row
 * is still what the rest of the app reads, so no existing screen is affected.
 */
@Entity(tableName = "explore_shops")
data class ExploreShop(
    @PrimaryKey val remoteId: String,          // Supabase shops.id (UUID)
    val name: String,
    val category: String,                      // Supermarket | Bakery | Pharmacy | Grocery | Other
    val district: String,
    val coverImageUrl: String = "",            // Supabase Storage public URL; "" = placeholder
    val ratingAverage: Double = 0.0,
    val ratingCount: Int = 0,
    val avgPrice: Double = 0.0,                // for ₣ / ₣₣ / ₣₣₣ price-range indicator
    val salesVelocity: Double = 0.0,           // recent sales per day (trending rank input)
    val productCount: Int = 0,
    val registeredAt: Long = 0,                // epoch millis — "New on Duka" ordering
    val isFlagged: Boolean = false,            // set by admins in the portal; hidden from Explore
    val syncedAt: Long = 0                     // local cache bookkeeping
)

/**
 * Phase 3 — Owner ↔ Client chat (cross-device via Supabase realtime).
 *
 * SCOPE-GUARD: NEW table. The V1/V2 owner↔employee chat_messages table is untouched.
 * Reuses the same message shape, with the participantType discriminator the spec asks for.
 * `remoteId` is the Supabase `messages.id` (UUID) or "" for locally-pending sends.
 */
@Entity(tableName = "client_chat_messages")
data class ClientChatMessage(
    @PrimaryKey val remoteId: String,          // Supabase messages.id (UUID); "" until pushed
    val conversationId: String,                // deterministic pair id (see ChatConversations)
    val shopId: String,                        // remote shop uuid — lets either side open the thread
    val clientUserId: Long,                    // the client side of the conversation
    val senderRole: String,                    // "owner" | "client"
    val participantType: String,               // "employee" | "client" (spec field)
    val senderLabel: String,                   // display name
    val text: String,
    val timestamp: Long,
    val pendingPush: Boolean = false           // true = not yet confirmed by server
)
