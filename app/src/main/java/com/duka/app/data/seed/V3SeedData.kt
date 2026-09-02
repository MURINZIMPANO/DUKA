package com.duka.app.data.seed

import com.duka.app.data.local.dao.*
import com.duka.app.data.local.entity.*

/**
 * V3/V4 seed data — demo data for Marketplace, Government, and Supply Network features.
 *
 * MOCK STATUS: All seeded data is local-mock. Distance values, ratings, wholesaler delivery
 * estimates, and government metrics are computed from or randomly seeded alongside existing
 * local seed data. No real external systems are involved.
 *
 * Demo access:
 * - Client login: Use phone "0788000001" (password "demo123") — client role
 * - Government admin: Use phone "0788000002" (password "demo123") — government_admin role
 */
object V3SeedData {

    const val DEMO_CLIENT_PHONE = "0788000001"
    const val DEMO_CLIENT_PASSWORD = "demo123"
    const val DEMO_CLIENT_NAME = "Amina Mutoni"

    const val DEMO_GOV_PHONE = "0788000002"
    const val DEMO_GOV_PASSWORD = "demo123"
    const val DEMO_GOV_NAME = "Admin Government"

    suspend fun seedV3Data(
        userDao: UserDao,
        purchaseDao: PurchaseDao,
        budgetGoalDao: BudgetGoalDao,
        feedbackReportDao: FeedbackReportDao,
        shopRatingDao: ShopRatingDao,
        wholesalerDao: WholesalerDao,
        productDao: ProductDao,
        businessDao: BusinessDao
    ) {
        // Check if already seeded
        val existingClient = userDao.getByPhoneOrEmail(DEMO_CLIENT_PHONE)
        if (existingClient != null) return

        // Get the existing demo business
        val business = businessDao.getActiveBusinessOnce() ?: return
        val businessId = business.id

        // Create demo client user — Amina Mutoni
        val clientPasswordHash = org.mindrot.jbcrypt.BCrypt.hashpw(DEMO_CLIENT_PASSWORD, org.mindrot.jbcrypt.BCrypt.gensalt())
        val clientUserId = userDao.insert(
            User(
                businessId = null, // Client is not tied to a specific business
                fullName = DEMO_CLIENT_NAME,
                phoneOrEmail = DEMO_CLIENT_PHONE,
                passwordHash = clientPasswordHash,
                role = "client"
            )
        )

        // Create demo government admin user
        val govPasswordHash = org.mindrot.jbcrypt.BCrypt.hashpw(DEMO_GOV_PASSWORD, org.mindrot.jbcrypt.BCrypt.gensalt())
        userDao.insert(
            User(
                businessId = null,
                fullName = DEMO_GOV_NAME,
                phoneOrEmail = DEMO_GOV_PHONE,
                passwordHash = govPasswordHash,
                role = "government_admin"
            )
        )

        // Seed demo purchases
        val products = productDao.getProductsByBusiness(businessId).let { flow ->
            var result: List<Product> = emptyList()
            flow.collect { result = it }
            result
        }

        if (products.isNotEmpty()) {
            val now = System.currentTimeMillis()
            // Create a few demo purchases for Amina
            repeat(minOf(3, products.size)) { i ->
                purchaseDao.insert(
                    Purchase(
                        clientUserId = clientUserId,
                        businessId = businessId,
                        productId = products[i].id,
                        amount = products[i].price,
                        timestamp = now - (i * 86400000L), // Spread over days
                        receiptNumber = "EBM-DEMO-${1001 + i}"
                    )
                )
            }

            // Seed budget goal for Amina: 25,000 RWF weekly limit
            budgetGoalDao.upsert(
                BudgetGoal(
                    clientUserId = clientUserId,
                    weeklyLimit = 25000.0
                )
            )
        }

        // Seed two feedback reports for Amina — one "Under review", one "Noted"
        feedbackReportDao.insert(
            FeedbackReport(
                clientUserId = clientUserId,
                targetBusinessId = businessId,
                category = "Suggestion",
                message = "Would love to see more fresh produce options in the store.",
                isAnonymous = false,
                status = "Under review"
            )
        )
        feedbackReportDao.insert(
            FeedbackReport(
                clientUserId = clientUserId,
                targetBusinessId = businessId,
                category = "Complaint",
                message = "Long waiting times at the checkout counter during peak hours.",
                isAnonymous = true,
                status = "Noted"
            )
        )

        // Seed shop rating
        shopRatingDao.upsert(
            ShopRating(
                businessId = businessId,
                averageRating = 4.2,
                ratingCount = 15
            )
        )

        // Seed wholesalers
        wholesalerDao.insert(
            Wholesaler(
                name = "Kigali Wholesale Hub",
                specialty = "Food & Beverages",
                rating = 4.5,
                deliveryEstimate = "1-2 days"
            )
        )
        wholesalerDao.insert(
            Wholesaler(
                name = "Rwanda Supply Co.",
                specialty = "Household & Personal Care",
                rating = 4.2,
                deliveryEstimate = "2-3 days"
            )
        )
        wholesalerDao.insert(
            Wholesaler(
                name = "Fresh Produce Direct",
                specialty = "Fresh Food & Produce",
                rating = 4.8,
                deliveryEstimate = "Same day"
            )
        )
        wholesalerDao.insert(
            Wholesaler(
                name = "TechGoods Rwanda",
                specialty = "Electronics & Accessories",
                rating = 3.9,
                deliveryEstimate = "3-5 days"
            )
        )
    }
}
