package com.duka.app.data.seed

import com.duka.app.data.local.dao.*
import com.duka.app.data.local.entity.*
import org.mindrot.jbcrypt.BCrypt

/**
 * Seed data for first-run demo state.
 * Clearly separated from real user data.
 * Uses realistic sample data from the spec: Kigali Fresh Mart, Sugar 1kg, etc.
 *
 * Demo login credentials:
 *   Owner: phone "0788000000", password "demo123"
 *   Employee: code "EMP-1234", name "Demo Employee"
 */
object SeedData {

    private const val DEMO_BUSINESS_NAME = "Kigali Fresh Mart"
    private const val DEMO_BUSINESS_TYPE = "Supermarket"
    private const val DEMO_EMPLOYEE_COUNT = 3
    private const val DEMO_LANGUAGE = "English"
    private const val DEMO_DISTRICT = "Gasabo"

    // Demo owner credentials
    const val DEMO_OWNER_PHONE = "0788000000"
    const val DEMO_OWNER_PASSWORD = "demo123"
    const val DEMO_OWNER_NAME = "Jean Niyomwungeri"

    // Demo employee code
    const val DEMO_EMPLOYEE_CODE = "EMP-1234"
    const val DEMO_EMPLOYEE_NAME = "Demo Employee"

    data class SeedProduct(
        val name: String,
        val price: Double,
        val stockQuantity: Int,
        val category: String
    )

    private val seedProducts = listOf(
        SeedProduct("Sugar 1kg", 1500.0, 100, "Food"),
        SeedProduct("Rice 2kg", 3200.0, 50, "Food"),
        SeedProduct("Cooking Oil 1L", 2800.0, 30, "Food"),
        SeedProduct("Milk 500ml", 800.0, 60, "Beverages"),
        SeedProduct("Bread Loaf", 600.0, 40, "Food"),
        SeedProduct("Soap Bar", 500.0, 80, "Personal Care"),
        SeedProduct("Water 1.5L", 400.0, 120, "Beverages"),
        SeedProduct("Salt 1kg", 450.0, 70, "Food"),
        SeedProduct("Tea Leaves 250g", 1200.0, 45, "Beverages"),
        SeedProduct("Maize Flour 2kg", 1800.0, 35, "Food")
    )

    /**
     * Seeds the database on first launch with a fresh demo state.
     * Creates business, products, sales, employees, users, and chat messages.
     */
    suspend fun seedFreshDemo(
        businessDao: BusinessDao,
        productDao: ProductDao,
        saleDao: SaleDao,
        employeeDao: EmployeeDao,
        chatMessageDao: ChatMessageDao,
        userDao: UserDao
    ) {
        val existing = businessDao.getActiveBusinessOnce()
        if (existing != null) return // Already has data

        // Create demo business
        val businessId = businessDao.insert(
            Business(
                name = DEMO_BUSINESS_NAME,
                type = DEMO_BUSINESS_TYPE,
                employeeCount = DEMO_EMPLOYEE_COUNT,
                language = DEMO_LANGUAGE,
                district = DEMO_DISTRICT
            )
        )

        // Create demo owner user (password: demo123)
        val ownerPasswordHash = BCrypt.hashpw(DEMO_OWNER_PASSWORD, BCrypt.gensalt())
        userDao.insert(
            User(
                businessId = businessId,
                fullName = DEMO_OWNER_NAME,
                phoneOrEmail = DEMO_OWNER_PHONE,
                passwordHash = ownerPasswordHash,
                role = "owner"
            )
        )

        // Create demo products
        val productIds = seedProducts.map { seed ->
            productDao.insert(
                Product(
                    businessId = businessId,
                    name = seed.name,
                    price = seed.price,
                    stockQuantity = seed.stockQuantity,
                    category = seed.category
                )
            )
        }

        // Create demo sales for today
        val now = System.currentTimeMillis()
        val todayStart = now - (now % (24 * 60 * 60 * 1000))

        saleDao.insert(
            Sale(productId = productIds[0], businessId = businessId, amount = 1500.0, timestamp = todayStart + 3600000, source = "owner")
        )
        saleDao.insert(
            Sale(productId = productIds[2], businessId = businessId, amount = 2800.0, timestamp = todayStart + 7200000, source = "employee")
        )
        saleDao.insert(
            Sale(productId = productIds[4], businessId = businessId, amount = 600.0, timestamp = todayStart + 10800000, source = "owner")
        )

        // Create demo employee record (V5+ fields: userId, role, isActive, joinedAt, lastSeenAt)
        val demoUserId = userDao.insert(
            User(
                businessId = businessId,
                fullName = DEMO_EMPLOYEE_NAME,
                phoneOrEmail = DEMO_EMPLOYEE_NAME, // Employees log in by code, not email/password
                passwordHash = "",
                role = "employee"
            )
        )
        employeeDao.insert(
            Employee(
                businessId = businessId,
                userId = demoUserId,
                code = DEMO_EMPLOYEE_CODE,
                name = DEMO_EMPLOYEE_NAME,
                role = "cashier",
                isActive = true,
                joinedAt = System.currentTimeMillis(),
                lastSeenAt = null
            )
        )

        // Create a demo chat message
        chatMessageDao.insert(
            ChatMessage(
                businessId = businessId,
                senderRole = "owner",
                senderLabel = "Owner",
                text = "Welcome to Duka! This is your demo shop."
            )
        )
    }
}
