package com.duka.intelligence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.duka.app.MainActivity
import com.duka.app.data.local.dao.BusinessDao
import com.duka.app.data.local.dao.ProductAlertDao
import com.duka.app.data.local.dao.ProductDao
import com.duka.app.data.local.dao.SaleDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * WorkManager-based worker that runs daily to analyze stock levels and generate alerts.
 *
 * Runs on a PeriodicWorkRequest with daily frequency.
 * Reads Room directly, generates ProductAlert rows, posts system notifications for critical alerts.
 */
@HiltWorker
class StockIntelligenceWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val productDao: ProductDao,
    private val saleDao: SaleDao,
    private val businessDao: BusinessDao,
    private val productAlertDao: ProductAlertDao
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "stock_intelligence_daily"
        const val CHANNEL_ID = "duka_stock_alerts"
        const val CHANNEL_NAME = "Stock Alerts"
    }

    override suspend fun doWork(): Result {
        return try {
            val business = businessDao.getActiveBusinessOnce() ?: return Result.success()
            val products = productDao.getAllActiveByBusiness(business.id)
            val sales = saleDao.getAllByBusiness(business.id)

            // Generate alerts using pure functions
            val alerts = ProductIntelligence.generateAllAlerts(products, sales, business.id)

            // Clear old alerts of the same type to avoid duplicates
            for (alert in alerts) {
                val existing = productAlertDao.getUnreadByProductOnce(business.id, alert.productId)
                if (existing == null || existing.type != alert.type) {
                    productAlertDao.insert(alert)
                }
            }

            // Post system notifications for critical alerts only
            ensureNotificationChannel()
            for (alert in alerts.filter { it.severity == "critical" }) {
                val product = products.find { it.id == alert.productId }
                if (product != null) {
                    postCriticalNotification(product.id, product.name)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun ensureNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Alerts when products run low or out of stock"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun postCriticalNotification(productId: Long, productName: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("deep_link", "edit_product/$productId")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            productId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Stock Alert")
            .setContentText("$productName is out of stock — restock now")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(productId.toInt(), notification)
    }
}
