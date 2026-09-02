package com.duka.android.data.di

import com.duka.android.data.repository.AndroidAppNotificationRepository
import com.duka.android.data.repository.AndroidBudgetGoalRepository
import com.duka.android.data.repository.AndroidBusinessRepository
import com.duka.android.data.repository.AndroidChatRepository
import com.duka.android.data.repository.AndroidEbmRepository
import com.duka.android.data.repository.AndroidEmployeeRepository
import com.duka.android.data.repository.AndroidExpenseRepository
import com.duka.android.data.repository.AndroidFeedbackReportRepository
import com.duka.android.data.repository.AndroidIssueRepository
import com.duka.android.data.repository.AndroidProductAlertRepository
import com.duka.android.data.repository.AndroidProductRepository
import com.duka.android.data.repository.AndroidPromoRepository
import com.duka.android.data.repository.AndroidPurchaseRepository
import com.duka.android.data.repository.AndroidRestockRequestRepository
import com.duka.android.data.repository.AndroidSaleRepository
import com.duka.android.data.repository.AndroidShopRatingRepository
import com.duka.android.data.repository.AndroidStockAdjustmentRepository
import com.duka.android.data.repository.AndroidTaxRepository
import com.duka.android.data.repository.AndroidUserRepository
import com.duka.android.data.repository.AndroidWholesalerRepository
import com.duka.shared.data.repository.AppNotificationRepository
import com.duka.shared.data.repository.BudgetGoalRepository
import com.duka.shared.data.repository.BusinessRepository
import com.duka.shared.data.repository.ChatRepository
import com.duka.shared.data.repository.EbmRepository
import com.duka.shared.data.repository.EmployeeRepository
import com.duka.shared.data.repository.ExpenseRepository
import com.duka.shared.data.repository.FeedbackReportRepository
import com.duka.shared.data.repository.IssueRepository
import com.duka.shared.data.repository.ProductAlertRepository
import com.duka.shared.data.repository.ProductRepository
import com.duka.shared.data.repository.PromoRepository
import com.duka.shared.data.repository.PurchaseRepository
import com.duka.shared.data.repository.RestockRequestRepository
import com.duka.shared.data.repository.SaleRepository
import com.duka.shared.data.repository.ShopRatingRepository
import com.duka.shared.data.repository.StockAdjustmentRepository
import com.duka.shared.data.repository.TaxRepository
import com.duka.shared.data.repository.UserRepository
import com.duka.shared.data.repository.WholesalerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds shared repository interface implementations.
 * These implementations delegate to the existing Room DAOs via mappers.
 * This allows shared ViewModels to depend on shared interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SharedRepositoryModule {

    @Binds @Singleton
    abstract fun bindBusinessRepository(impl: AndroidBusinessRepository): BusinessRepository

    @Binds @Singleton
    abstract fun bindUserRepository(impl: AndroidUserRepository): UserRepository

    @Binds @Singleton
    abstract fun bindProductRepository(impl: AndroidProductRepository): ProductRepository

    @Binds @Singleton
    abstract fun bindSaleRepository(impl: AndroidSaleRepository): SaleRepository

    @Binds @Singleton
    abstract fun bindEmployeeRepository(impl: AndroidEmployeeRepository): EmployeeRepository

    @Binds @Singleton
    abstract fun bindChatRepository(impl: AndroidChatRepository): ChatRepository

    @Binds @Singleton
    abstract fun bindPromoRepository(impl: AndroidPromoRepository): PromoRepository

    @Binds @Singleton
    abstract fun bindIssueRepository(impl: AndroidIssueRepository): IssueRepository

    @Binds @Singleton
    abstract fun bindEbmRepository(impl: AndroidEbmRepository): EbmRepository

    @Binds @Singleton
    abstract fun bindTaxRepository(impl: AndroidTaxRepository): TaxRepository

    @Binds @Singleton
    abstract fun bindPurchaseRepository(impl: AndroidPurchaseRepository): PurchaseRepository

    @Binds @Singleton
    abstract fun bindBudgetGoalRepository(impl: AndroidBudgetGoalRepository): BudgetGoalRepository

    @Binds @Singleton
    abstract fun bindFeedbackReportRepository(impl: AndroidFeedbackReportRepository): FeedbackReportRepository

    @Binds @Singleton
    abstract fun bindShopRatingRepository(impl: AndroidShopRatingRepository): ShopRatingRepository

    @Binds @Singleton
    abstract fun bindWholesalerRepository(impl: AndroidWholesalerRepository): WholesalerRepository

    @Binds @Singleton
    abstract fun bindRestockRequestRepository(impl: AndroidRestockRequestRepository): RestockRequestRepository

    @Binds @Singleton
    abstract fun bindStockAdjustmentRepository(impl: AndroidStockAdjustmentRepository): StockAdjustmentRepository

    @Binds @Singleton
    abstract fun bindProductAlertRepository(impl: AndroidProductAlertRepository): ProductAlertRepository

    @Binds @Singleton
    abstract fun bindAppNotificationRepository(impl: AndroidAppNotificationRepository): AppNotificationRepository

    @Binds @Singleton
    abstract fun bindExpenseRepository(impl: AndroidExpenseRepository): ExpenseRepository
}
