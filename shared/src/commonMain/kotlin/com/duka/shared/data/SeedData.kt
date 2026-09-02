package com.duka.shared.data

import com.duka.shared.domain.*

/**
 * Shared seed data for first-run demo state on both Android and iOS.
 * Uses the shared domain models directly — no platform dependencies.
 *
 * Demo login credentials:
 *   Owner:     0788000001  /  demo1234
 *   Employee:  code JB7X2K
 *   Client:    0788000002  /  client1234
 *
 * NOTE: Password hashes are pre-computed. The seed data module uses
 * PasswordHasher.checkPassword() which handles both Android (BCrypt)
 * and iOS (PBKDF2) hash formats at runtime.
 */
object SeedData {

    // ── Demo credentials ──────────────────────────────
    const val DEMO_OWNER_PHONE = "0788000001"
    const val DEMO_OWNER_PASSWORD = "demo1234"
    const val DEMO_OWNER_NAME = "Amina Owuor"

    const val DEMO_EMPLOYEE_CODE = "JB7X2K"
    const val DEMO_EMPLOYEE_NAME = "Jean Bosco"

    const val DEMO_CLIENT_PHONE = "0788000002"
    const val DEMO_CLIENT_PASSWORD = "client1234"
    const val DEMO_CLIENT_NAME = "Amina Mutoni"

    // ── Business ──────────────────────────────────────

    val demoBusiness = Business(
        id = 1,
        name = "Kigali Fresh Mart",
        type = "Supermarket",
        employeeCount = 3,
        language = "English",
        district = "Gasabo",
        createdAt = 1700000000000L
    )

    // ── Users ─────────────────────────────────────────
    // Password hashes will be computed at seed time using PasswordHasher.hashPassword()

    val demoOwnerTemplate = User(
        id = 1,
        businessId = 1,
        fullName = DEMO_OWNER_NAME,
        phoneOrEmail = DEMO_OWNER_PHONE,
        passwordHash = "", // computed at seed time
        role = "owner",
        isDeleted = false,
        createdAt = 1700000000000L
    )

    val demoEmployeeUserTemplate = User(
        id = 2,
        businessId = 1,
        fullName = DEMO_EMPLOYEE_NAME,
        phoneOrEmail = "",
        passwordHash = "",
        role = "employee",
        isDeleted = false,
        createdAt = 1700000000000L
    )

    val demoClientTemplate = User(
        id = 3,
        businessId = null,
        fullName = DEMO_CLIENT_NAME,
        phoneOrEmail = DEMO_CLIENT_PHONE,
        passwordHash = "", // computed at seed time
        role = "client",
        isDeleted = false,
        createdAt = 1700000000000L
    )

    // ── Employee record ───────────────────────────────

    val demoEmployee = Employee(
        id = 1,
        businessId = 1,
        userId = 2,
        code = DEMO_EMPLOYEE_CODE,
        name = DEMO_EMPLOYEE_NAME,
        role = "cashier",
        isActive = true,
        joinedAt = 1700000000000L,
        lastSeenAt = null
    )

    // ── Products ──────────────────────────────────────

    val demoProducts = listOf(
        Product(id = 1, businessId = 1, name = "Sugar 1kg",
            price = 1400.0, stockQuantity = 45, category = "Grocery",
            createdAt = 1700000000000L, costPrice = 1100, isDeleted = false, lowStockThreshold = 5),
        Product(id = 2, businessId = 1, name = "Cooking Oil 1L",
            price = 2800.0, stockQuantity = 12, category = "Grocery",
            createdAt = 1700000000000L, costPrice = 2200, isDeleted = false, lowStockThreshold = 5),
        Product(id = 3, businessId = 1, name = "Bread Loaf",
            price = 900.0, stockQuantity = 8, category = "Bakery",
            createdAt = 1700000000000L, costPrice = 650, isDeleted = false, lowStockThreshold = 3),
        Product(id = 4, businessId = 1, name = "Milk 500ml",
            price = 600.0, stockQuantity = 2, category = "Grocery",  // low stock — triggers alert
            createdAt = 1700000000000L, costPrice = 450, isDeleted = false, lowStockThreshold = 5),
        Product(id = 5, businessId = 1, name = "Paracetamol 500mg",
            price = 300.0, stockQuantity = 30, category = "Pharmacy",
            createdAt = 1699000000000L, // older than 14 days — triggers slow mover check
            costPrice = 200, isDeleted = false, lowStockThreshold = 10),
        Product(id = 6, businessId = 1, name = "Rice 2kg",
            price = 3200.0, stockQuantity = 50, category = "Grocery",
            createdAt = 1700000000000L, costPrice = 2500, isDeleted = false, lowStockThreshold = 10),
        Product(id = 7, businessId = 1, name = "Soap Bar",
            price = 500.0, stockQuantity = 80, category = "Personal Care",
            createdAt = 1700000000000L, costPrice = 350, isDeleted = false, lowStockThreshold = 15),
        Product(id = 8, businessId = 1, name = "Water 1.5L",
            price = 400.0, stockQuantity = 120, category = "Beverages",
            createdAt = 1700000000000L, costPrice = 250, isDeleted = false, lowStockThreshold = 20),
        Product(id = 9, businessId = 1, name = "Salt 1kg",
            price = 450.0, stockQuantity = 70, category = "Grocery",
            createdAt = 1700000000000L, costPrice = 300, isDeleted = false, lowStockThreshold = 15),
        Product(id = 10, businessId = 1, name = "Tea Leaves 250g",
            price = 1200.0, stockQuantity = 45, category = "Beverages",
            createdAt = 1700000000000L, costPrice = 850, isDeleted = false, lowStockThreshold = 10)
    )

    // ── Sales — 30 days of realistic data ─────────────

    fun buildDemoSales(): List<Sale> {
        val sales = mutableListOf<Sale>()
        val now = currentTimeMillis()
        val dayMs = 86400000L
        val productIds = listOf(1L, 2L, 3L, 6L, 7L, 8L, 9L, 10L)
        val amounts = mapOf(
            1L to 1400.0, 2L to 2800.0, 3L to 900.0, 6L to 3200.0,
            7L to 500.0, 8L to 400.0, 9L to 450.0, 10L to 1200.0
        )
        val sources = listOf("owner", "employee", "client")
        var saleId = 1L

        for (daysAgo in 0..29) {
            val dayStart = now - (daysAgo * dayMs)
            val salesThisDay = (3..12).random()
            repeat(salesThisDay) {
                val productId = productIds.random()
                val amount = amounts[productId] ?: 1000.0
                sales.add(
                    Sale(
                        id = saleId++,
                        productId = productId,
                        businessId = 1,
                        amount = amount,
                        timestamp = dayStart + (0..dayMs).random(),
                        source = sources.random()
                    )
                )
            }
        }
        return sales
    }

    // ── Budget goal ───────────────────────────────────

    val demoBudgetGoal = BudgetGoal(
        id = 1,
        clientUserId = 3,
        weeklyLimit = 25000.0,
        createdAt = 1700000000000L
    )

    // ── Feedback reports ──────────────────────────────

    val demoFeedbackReports = listOf(
        FeedbackReport(
            id = 1, clientUserId = 3, targetBusinessId = 1,
            category = "Suggestion",
            message = "Would be great to have more dairy products",
            isAnonymous = false, status = "Under review",
            createdAt = 1700100000000L
        ),
        FeedbackReport(
            id = 2, clientUserId = 3, targetBusinessId = 1,
            category = "Complaint",
            message = "Cooking oil was out of stock twice this week",
            isAnonymous = true, status = "Noted",
            createdAt = 1700200000000L
        )
    )

    // ── Shop rating ───────────────────────────────────

    val demoShopRating = ShopRating(
        id = 1,
        businessId = 1,
        averageRating = 4.2,
        ratingCount = 15
    )
}
