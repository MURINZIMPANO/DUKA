package com.duka.app.domain

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for parsing voice input into structured product data.
 * Designed to be swappable — regex now, LLM-based later.
 */
interface VoiceEntryParser {
    fun parse(transcript: String): ParsedProduct

    data class ParsedProduct(
        val productName: String?,
        val price: Double?,
        val quantity: Int?,
        val sizeDetail: String?,
        val rawTranscript: String
    )
}

/**
 * Rule-based/regex parser for Kinyarwanda and English voice input.
 * Assumption: Voice input follows patterns like
 *   "Sugar 1kg price 1500" or "Igisura ingano 5 igiciro 2000"
 */
@Singleton
class RegexVoiceEntryParser @Inject constructor() : VoiceEntryParser {

    // Regex patterns for price extraction
    private val pricePatterns = listOf(
        Regex("""(?:price|igiciro|cost)\s*[:=]?\s*(\d[\d,]*)""", RegexOption.IGNORE_CASE),
        Regex("""(\d[\d,]*)\s*(?:RWF|rwf|frw|FRW)""", RegexOption.IGNORE_CASE),
        Regex("""(\d[\d,]*)\s*(?:francs?)""", RegexOption.IGNORE_CASE)
    )

    // Regex patterns for quantity/size extraction
    private val quantityPatterns = listOf(
        Regex("""(\d+)\s*(?:pcs?|pieces?|count|ingano)""", RegexOption.IGNORE_CASE),
        Regex("""qty\s*[:=]?\s*(\d+)""", RegexOption.IGNORE_CASE)
    )

    private val sizePatterns = listOf(
        Regex("""(\d+(?:\.\d+)?)\s*(?:kg|g|ml|l|liters?|ibiro|garamu)""", RegexOption.IGNORE_CASE),
        Regex("""size\s*[:=]?\s*(\S+)""", RegexOption.IGNORE_CASE),
        Regex("""ingano\s*[:=]?\s*(\S+)""", RegexOption.IGNORE_CASE)
    )

    override fun parse(transcript: String): VoiceEntryParser.ParsedProduct {
        var price: Double? = null
        var quantity: Int? = null
        var sizeDetail: String? = null

        // Extract price
        for (pattern in pricePatterns) {
            val match = pattern.find(transcript)
            if (match != null) {
                price = match.groupValues[1].replace(",", "").toDoubleOrNull()
                break
            }
        }

        // Extract quantity
        for (pattern in quantityPatterns) {
            val match = pattern.find(transcript)
            if (match != null) {
                quantity = match.groupValues[1].toIntOrNull()
                break
            }
        }

        // Extract size detail
        for (pattern in sizePatterns) {
            val match = pattern.find(transcript)
            if (match != null) {
                sizeDetail = match.groupValues[1] + " " +
                    (match.groupValues.getOrNull(2) ?: "")
                break
            }
        }

        // Extract product name: take the first meaningful words before any keyword
        val productName = extractProductName(transcript)

        return VoiceEntryParser.ParsedProduct(
            productName = productName,
            price = price,
            quantity = quantity,
            sizeDetail = sizeDetail,
            rawTranscript = transcript
        )
    }

    private fun extractProductName(transcript: String): String {
        // Remove known keywords and price/quantity info, what remains is likely the product name
        val cleaned = transcript
            .replace(Regex("""(?:price|igiciro|cost|qty|size|ingano)\s*[:=]?\s*\S*""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""\d[\d,]*\s*(?:RWF|rwf|frw|FRW|francs?)""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""\d+\s*(?:kg|g|ml|l|liters?|ibiro|garamu|pcs?|pieces?|count|ingano)""", RegexOption.IGNORE_CASE), "")
            .trim()

        return cleaned.ifBlank { null }
            ?: transcript.split(Regex("\\s+")).take(2).joinToString(" ")
    }
}
