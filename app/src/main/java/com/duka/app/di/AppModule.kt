package com.duka.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.room.Room
import com.duka.app.data.local.DukaDatabase
import com.duka.app.data.local.dao.*
import com.duka.app.data.session.SessionManager
import com.duka.app.domain.EbmGateway
import com.duka.app.domain.MockEbmGateway
import com.duka.app.domain.RegexVoiceEntryParser
import com.duka.app.domain.VoiceEntryParser
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "duka_session")

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DukaDatabase {
        return Room.databaseBuilder(
            context,
            DukaDatabase::class.java,
            "duka.db"
        )
            .addMigrations(DukaDatabase.MIGRATION_3_4, DukaDatabase.MIGRATION_4_5, DukaDatabase.MIGRATION_5_6)
            .build()
    }

    @Provides fun provideBusinessDao(db: DukaDatabase): BusinessDao = db.businessDao()
    @Provides fun provideProductDao(db: DukaDatabase): ProductDao = db.productDao()
    @Provides fun provideSaleDao(db: DukaDatabase): SaleDao = db.saleDao()
    @Provides fun provideEmployeeDao(db: DukaDatabase): EmployeeDao = db.employeeDao()
    @Provides fun provideChatMessageDao(db: DukaDatabase): ChatMessageDao = db.chatMessageDao()
    @Provides fun providePromoDao(db: DukaDatabase): PromoDao = db.promoDao()
    @Provides fun provideIssueReportDao(db: DukaDatabase): IssueReportDao = db.issueReportDao()
    @Provides fun provideEbmReceiptDao(db: DukaDatabase): EbmReceiptDao = db.ebmReceiptDao()
    @Provides fun provideTaxProfileDao(db: DukaDatabase): TaxProfileDao = db.taxProfileDao()
    @Provides fun provideUserDao(db: DukaDatabase): UserDao = db.userDao()

    // V3/V4 DAOs
    @Provides fun providePurchaseDao(db: DukaDatabase): PurchaseDao = db.purchaseDao()
    @Provides fun provideBudgetGoalDao(db: DukaDatabase): BudgetGoalDao = db.budgetGoalDao()
    @Provides fun provideFeedbackReportDao(db: DukaDatabase): FeedbackReportDao = db.feedbackReportDao()
    @Provides fun provideShopRatingDao(db: DukaDatabase): ShopRatingDao = db.shopRatingDao()
    @Provides fun provideWholesalerDao(db: DukaDatabase): WholesalerDao = db.wholesalerDao()
    @Provides fun provideRestockRequestDao(db: DukaDatabase): RestockRequestDao = db.restockRequestDao()

    // V5 DAOs
    @Provides fun provideStockAdjustmentDao(db: DukaDatabase): StockAdjustmentDao = db.stockAdjustmentDao()
    @Provides fun provideProductAlertDao(db: DukaDatabase): ProductAlertDao = db.productAlertDao()
    @Provides fun provideAppNotificationDao(db: DukaDatabase): AppNotificationDao = db.appNotificationDao()
    @Provides fun provideExpenseDao(db: DukaDatabase): ExpenseDao = db.expenseDao()
}

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideSessionManager(
        dataStore: DataStore<Preferences>,
        userDao: UserDao
    ): SessionManager {
        return SessionManager(dataStore, userDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindEbmGateway(impl: MockEbmGateway): EbmGateway

    @Binds
    @Singleton
    abstract fun bindVoiceEntryParser(impl: RegexVoiceEntryParser): VoiceEntryParser
}
