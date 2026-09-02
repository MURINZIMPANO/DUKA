package com.duka.android.data.di

import com.duka.app.data.local.DukaDatabase
import com.duka.android.data.repository.*
import com.duka.shared.data.repository.*
import com.duka.shared.viewmodel.SessionViewModel
import org.koin.dsl.module

/**
 * Android Koin module — provides shared repository implementations and shared ViewModels.
 * This module is loaded alongside Hilt in DukaApplication.
 *
 * Hilt handles Android-framework objects (Activities, Fragments, WorkManager).
 * Koin handles shared cross-platform ViewModels and repositories.
 *
 * The Room database is provided via Room.databaseBuilder (not Koin or Hilt)
 * and passed directly to this module. DAOs are extracted from it.
 */
fun androidRepositoryModule(database: DukaDatabase) = module {
    // DAOs extracted from the Room database
    single { database.businessDao() }
    single { database.userDao() }
    single { database.productDao() }
    single { database.saleDao() }
    single { database.employeeDao() }
    single { database.chatMessageDao() }
    single { database.promoDao() }
    single { database.issueReportDao() }
    single { database.ebmReceiptDao() }
    single { database.taxProfileDao() }
    single { database.purchaseDao() }
    single { database.budgetGoalDao() }
    single { database.feedbackReportDao() }
    single { database.shopRatingDao() }
    single { database.wholesalerDao() }
    single { database.restockRequestDao() }
    single { database.stockAdjustmentDao() }
    single { database.productAlertDao() }
    single { database.appNotificationDao() }
    single { database.expenseDao() }

    // Shared repository implementations
    single<BusinessRepository> { AndroidBusinessRepository(get()) }
    single<UserRepository> { AndroidUserRepository(get()) }
    single<ProductRepository> { AndroidProductRepository(get()) }
    single<SaleRepository> { AndroidSaleRepository(get()) }
    single<EmployeeRepository> { AndroidEmployeeRepository(get()) }
    single<ChatRepository> { AndroidChatRepository(get()) }
    single<PromoRepository> { AndroidPromoRepository(get()) }
    single<IssueRepository> { AndroidIssueRepository(get()) }
    single<EbmRepository> { AndroidEbmRepository(get()) }
    single<TaxRepository> { AndroidTaxRepository(get()) }
    single<PurchaseRepository> { AndroidPurchaseRepository(get()) }
    single<BudgetGoalRepository> { AndroidBudgetGoalRepository(get()) }
    single<FeedbackReportRepository> { AndroidFeedbackReportRepository(get()) }
    single<ShopRatingRepository> { AndroidShopRatingRepository(get()) }
    single<WholesalerRepository> { AndroidWholesalerRepository(get()) }
    single<RestockRequestRepository> { AndroidRestockRequestRepository(get()) }
    single<StockAdjustmentRepository> { AndroidStockAdjustmentRepository(get()) }
    single<ProductAlertRepository> { AndroidProductAlertRepository(get()) }
    single<AppNotificationRepository> { AndroidAppNotificationRepository(get()) }
    single<ExpenseRepository> { AndroidExpenseRepository(get()) }
}

val androidViewModelModule = module {
    single {
        SessionViewModel(
            sessionManager = get(),
            userRepository = get(),
            businessRepository = get(),
            employeeRepository = get()
        )
    }
}
