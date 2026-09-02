package com.duka.shared.domain

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
 */
class RegexVoiceEntryParser : VoiceEntryParser {

    private val pricePatterns = listOf(
        Regex("""(?:price|igiciro|cost)\s*[:=]?\s*(\d[\d,]*)""", RegexOption.IGNORE_CASE),
        Regex("""(\d[\d,]*)\s*(?:RWF|rwf|frw|FRW)""", RegexOption.IGNORE_CASE),
        Regex("""(\d[\d,]*)\s*(?:francs?)""", RegexOption.IGNORE_CASE)
    )

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

        for (pattern in pricePatterns) {
            val match = pattern.find(transcript)
            if (match != null) {
                price = match.groupValues[1].replace(",", "").toDoubleOrNull()
                break
            }
        }

        for (pattern in quantityPatterns) {
            val match = pattern.find(transcript)
            if (match != null) {
                quantity = match.groupValues[1].toIntOrNull()
                break
            }
        }

        for (pattern in sizePatterns) {
            val match = pattern.find(transcript)
            if (match != null) {
                sizeDetail = match.groupValues[1] + " " +
                    (match.groupValues.getOrNull(2) ?: "")
                break
            }
        }

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
        val cleaned = transcript
            .replace(Regex("""(?:price|igiciro|cost|qty|size|ingano)\s*[:=]?\s*\S*""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""\d[\d,]*\s*(?:RWF|rwf|frw|FRW|francs?)""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""\d+\s*(?:kg|g|ml|l|liters?|ibiro|garamu|pcs?|pieces?|count|ingano)""", RegexOption.IGNORE_CASE), "")
            .trim()

        return cleaned.ifBlank { null }
            ?: transcript.split(Regex("\\s+")).take(2).joinToString(" ")
    }
}
