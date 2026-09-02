package com.duka.app

import com.duka.app.data.local.dao.*
import com.duka.app.data.seed.SeedData
import com.duka.app.data.seed.V3SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Initializes the app with seed data on first launch.
 * This runs asynchronously and doesn't block the UI.
 * Now includes V3/V4 seed data for Marketplace, Government, and Supply Network features.
 */
@Singleton
class DukaInitializer @Inject constructor(
    private val businessDao: BusinessDao,
    private val productDao: ProductDao,
    private val saleDao: SaleDao,
    private val employeeDao: EmployeeDao,
    private val chatMessageDao: ChatMessageDao,
    private val userDao: UserDao,
    // V3/V4 DAOs
    private val purchaseDao: PurchaseDao,
    private val budgetGoalDao: BudgetGoalDao,
    private val feedbackReportDao: FeedbackReportDao,
    private val shopRatingDao: ShopRatingDao,
    private val wholesalerDao: WholesalerDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var initialized = false

    fun initialize() {
        if (initialized) return
        initialized = true

        scope.launch {
            // V1/V2 seed data
            SeedData.seedFreshDemo(
                businessDao = businessDao,
                productDao = productDao,
                saleDao = saleDao,
                employeeDao = employeeDao,
                chatMessageDao = chatMessageDao,
                userDao = userDao
            )

            // V3/V4 seed data (depends on V1/V2 data being present)
            V3SeedData.seedV3Data(
                userDao = userDao,
                purchaseDao = purchaseDao,
                budgetGoalDao = budgetGoalDao,
                feedbackReportDao = feedbackReportDao,
                shopRatingDao = shopRatingDao,
                wholesalerDao = wholesalerDao,
                productDao = productDao,
                businessDao = businessDao
            )
        }
    }
}
