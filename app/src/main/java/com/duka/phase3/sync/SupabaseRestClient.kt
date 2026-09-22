package com.duka.phase3.sync

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Phase 3 — Minimal Supabase REST client.
 *
 * DELIBERATE CHOICE: implemented with HttpURLConnection + kotlinx.serialization,
 * both already in the project's dependency tree. Zero new Gradle dependencies so
 * the existing build is untouched. Swap for supabase-kt later if desired — call
 * sites only use [get], [post], [patch], [delete].
 */
class SupabaseRestClient(
    private val url: String = SupabaseConfig.restUrl,
    private val apiKey: String = SupabaseConfig.ANON_KEY
) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @Serializable
    data class RestError(val message: String, val code: String = "")

    sealed class Result {
        data class Success(val body: String, val code: Int) : Result()
        data class Failure(val message: String, val code: Int = 0, val retriable: Boolean) : Result()
    }

    suspend fun get(
        table: String,
        columns: String = "*",
        filters: Map<String, String> = emptyMap(),
        order: String? = null,
        limit: Int? = null
    ): Result = withContext(Dispatchers.IO) {
        val sb = StringBuilder("$url/$table?select=${enc(columns)}")
        filters.forEach { (k, v) -> sb.append("&").append(enc(k)).append("=").append(enc(v)) }
        order?.let { sb.append("&order=").append(enc(it)) }
        limit?.let { sb.append("&limit=").append(it) }
        request("GET", sb.toString())
    }

    suspend fun post(table: String, bodyJson: String, preferReturn: String = "representation"): Result =
        withContext(Dispatchers.IO) {
            request(
                "POST", "$url/$table", bodyJson,
                extraHeaders = mapOf(
                    "Prefer" to "return=$preferReturn",
                    "Content-Type" to "application/json"
                )
            )
        }

    suspend fun patch(table: String, filters: Map<String, String>, bodyJson: String): Result =
        withContext(Dispatchers.IO) {
            val sb = StringBuilder("$url/$table")
            filters.forEach { (k, v) -> sb.append(if (sb.last() == '/') "?" else "&").append(enc(k)).append("=").append(enc(v)) }
            request(
                "PATCH", sb.toString(), bodyJson,
                extraHeaders = mapOf("Prefer" to "return=representation", "Content-Type" to "application/json")
            )
        }

    suspend fun delete(table: String, filters: Map<String, String>): Result =
        withContext(Dispatchers.IO) {
            val sb = StringBuilder("$url/$table")
            filters.forEach { (k, v) -> sb.append(if (sb.last() == '/') "?" else "&").append(enc(k)).append("=").append(enc(v)) }
            request("DELETE", sb.toString())
        }

    private fun request(
        method: String,
        urlString: String,
        bodyJson: String? = null,
        extraHeaders: Map<String, String> = emptyMap()
    ): Result {
        return try {
            val conn = (URL(urlString).openConnection() as HttpURLConnection).apply {
                requestMethod = method
                setRequestProperty("apikey", apiKey)
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("Accept", "application/json")
                extraHeaders.forEach { (k, v) -> setRequestProperty(k, v) }
                connectTimeout = 10_000
                readTimeout = 15_000
                if (bodyJson != null) {
                    doOutput = true
                    outputStream.use { it.write(bodyJson.toByteArray(Charsets.UTF_8)) }
                }
            }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val body = stream?.bufferedReader()?.use { it.readText() } ?: ""
            conn.disconnect()
            if (code in 200..299) {
                Result.Success(body, code)
            } else {
                Result.Failure(
                    message = parseErrorMessage(body) ?: "HTTP $code",
                    code = code,
                    retriable = code >= 500 || code == 429 || code == 0
                )
            }
        } catch (e: IOException) {
            // No connectivity / timeout — retriable, never silent.
            Result.Failure(message = e.message ?: "Network error", code = 0, retriable = true)
        } catch (e: Exception) {
            Result.Failure(message = e.message ?: "Unexpected error", code = 0, retriable = false)
        }
    }

    private fun parseErrorMessage(body: String): String? = try {
        val el: JsonElement = json.parseToJsonElement(body)
        when (el) {
            is JsonObject -> el["message"]?.toString()?.trim('"')
            else -> null
        }
    } catch (_: Exception) {
        null
    }

    fun parseArray(body: String): JsonArray = try {
        json.parseToJsonElement(body).jsonArray
    } catch (_: Exception) {
        JsonArray(emptyList())
    }

    companion object {
        fun enc(value: String): String = URLEncoder.encode(value, "UTF-8")
            .replace("+", "%20")
    }
}
