package com.duka.app.domain

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Interface for the EBM (Electronic Billing Machine) gateway.
 * Clean abstraction so a real RRA Sales Data Controller API can be plugged in later.
 */
interface EbmGateway {
    /**
     * Attempt to send a sale receipt to the EBM system.
     * Returns a receipt number if the send was accepted (even locally).
     */
    suspend fun sendReceipt(saleId: Long, amount: Double, productName: String): EbmResult

    data class EbmResult(
        val success: Boolean,
        val receiptNumber: String,
        val message: String
    )
}

/**
 * Local mock implementation of EbmGateway.
 * Generates a fake receipt number and always returns "sent" status.
 *
 * // TODO: replace MockEbmGateway with real RRA Sales Data Controller API once vendor-certified
 */
@Singleton
class MockEbmGateway @Inject constructor() : EbmGateway {

    override suspend fun sendReceipt(saleId: Long, amount: Double, productName: String): EbmGateway.EbmResult {
        // Simulate network delay (instant for mock)
        val receiptNumber = generateReceiptNumber()
        return EbmGateway.EbmResult(
            success = true,
            receiptNumber = receiptNumber,
            message = "Receipt $receiptNumber generated (mock)"
        )
    }

    private fun generateReceiptNumber(): String {
        val timestamp = System.currentTimeMillis().toString(36).uppercase()
        val random = Random.nextInt(1000, 9999)
        return "EBM-$timestamp-$random"
    }
}
