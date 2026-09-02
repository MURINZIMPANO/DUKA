package com.duka.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duka.app.data.local.dao.*
import com.duka.app.data.local.entity.*

@Database(
    entities = [
        // V1/V2 entities
        Business::class,
        Product::class,
        Sale::class,
        Employee::class,
        ChatMessage::class,
        Promo::class,
        IssueReport::class,
        EbmReceipt::class,
        TaxProfile::class,
        User::class,
        // V3 entities
        Purchase::class,
        BudgetGoal::class,
        FeedbackReport::class,
        ShopRating::class,
        // V4 entities
        Wholesaler::class,
        RestockRequest::class,
        // V5 entities
        StockAdjustment::class,
        ProductAlert::class,
        AppNotification::class,
        Expense::class
    ],
    version = 6,
    exportSchema = false
)
abstract class DukaDatabase : RoomDatabase() {
    // V1/V2 DAOs
    abstract fun businessDao(): BusinessDao
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun promoDao(): PromoDao
    abstract fun issueReportDao(): IssueReportDao
    abstract fun ebmReceiptDao(): EbmReceiptDao
    abstract fun taxProfileDao(): TaxProfileDao
    abstract fun userDao(): UserDao

    // V3/V4 DAOs
    abstract fun purchaseDao(): PurchaseDao
    abstract fun budgetGoalDao(): BudgetGoalDao
    abstract fun feedbackReportDao(): FeedbackReportDao
    abstract fun shopRatingDao(): ShopRatingDao
    abstract fun wholesalerDao(): WholesalerDao
    abstract fun restockRequestDao(): RestockRequestDao

    // V5 DAOs
    abstract fun stockAdjustmentDao(): StockAdjustmentDao
    abstract fun productAlertDao(): ProductAlertDao
    abstract fun appNotificationDao(): AppNotificationDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        /**
         * Migration 3 → 4: Add Employee management columns.
         */
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val now = System.currentTimeMillis()
                db.execSQL("ALTER TABLE employees ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE employees ADD COLUMN role TEXT NOT NULL DEFAULT 'cashier'")
                db.execSQL("ALTER TABLE employees ADD COLUMN isActive INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE employees ADD COLUMN joinedAt INTEGER NOT NULL DEFAULT $now")
                db.execSQL("ALTER TABLE employees ADD COLUMN lastSeenAt INTEGER")
            }
        }

        /**
         * Migration 4 → 5: V5 Product Management, Stock Intelligence, Notifications, Analytics.
         *
         * Adds:
         * - Product columns: isDeleted, costPrice, lowStockThreshold
         * - New tables: stock_adjustments, product_alerts, app_notifications, expenses
         */
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add new columns to products table
                db.execSQL("ALTER TABLE products ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE products ADD COLUMN costPrice INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE products ADD COLUMN lowStockThreshold INTEGER NOT NULL DEFAULT 5")

                // Create stock_adjustments table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS stock_adjustments (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        productId INTEGER NOT NULL,
                        businessId INTEGER NOT NULL,
                        delta INTEGER NOT NULL,
                        reason TEXT NOT NULL,
                        timestamp INTEGER NOT NULL
                    )
                """.trimIndent())

                // Create product_alerts table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS product_alerts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        businessId INTEGER NOT NULL,
                        productId INTEGER NOT NULL,
                        type TEXT NOT NULL,
                        message TEXT NOT NULL,
                        severity TEXT NOT NULL,
                        isRead INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())

                // Create app_notifications table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS app_notifications (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        businessId INTEGER NOT NULL,
                        type TEXT NOT NULL,
                        title TEXT NOT NULL,
                        body TEXT NOT NULL,
                        actionRoute TEXT NOT NULL,
                        isRead INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())

                // Create expenses table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS expenses (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        businessId INTEGER NOT NULL,
                        label TEXT NOT NULL,
                        amount INTEGER NOT NULL,
                        periodStart INTEGER NOT NULL,
                        periodEnd INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }

        /**
         * Migration 5 → 6: Add soft-delete flag to users table for account deletion.
         */
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
