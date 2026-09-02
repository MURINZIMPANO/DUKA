package com.duka.shared.domain

interface EbmGateway {
    suspend fun sendReceipt(saleId: Long, amount: Double, productName: String): EbmResult

    data class EbmResult(
        val success: Boolean,
        val receiptNumber: String,
        val message: String
    )
}

class MockEbmGateway : EbmGateway {
    override suspend fun sendReceipt(saleId: Long, amount: Double, productName: String): EbmGateway.EbmResult {
        val receiptNumber = generateReceiptNumber()
        return EbmGateway.EbmResult(
            success = true,
            receiptNumber = receiptNumber,
            message = "Receipt $receiptNumber generated (mock)"
        )
    }

    private fun generateReceiptNumber(): String {
        val timestamp = currentTimeMillis().toString(36).uppercase()
        val random = (1000..9999).random()
        return "EBM-$timestamp-$random"
    }
}
