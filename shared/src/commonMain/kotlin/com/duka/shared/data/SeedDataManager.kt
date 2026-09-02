package com.duka.shared.data

import com.duka.shared.data.repository.*
import com.duka.shared.domain.PasswordHasher

/**
 * Shared SeedDataManager — seeds demo data on first launch.
 * Works on both Android and iOS via shared interfaces.
 *
 * The seed is idempotent: it checks a settings flag and skips if already seeded.
 * Password hashes are computed at runtime using the platform's PasswordHasher.
 */
class SeedDataManager(
    private val businessRepository: BusinessRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val employeeRepository: EmployeeRepository,
    private val feedbackReportRepository: FeedbackReportRepository,
    private val budgetGoalRepository: BudgetGoalRepository,
    private val shopRatingRepository: ShopRatingRepository,
    private val chatRepository: ChatRepository,
    private val settings: com.russhwolf.settings.ObservableSettings
) {
    private val seedKey = "seed_data_v2_inserted"

    /**
     * Seeds the database with demo data if not already done.
     * Safe to call multiple times — will only seed once.
     */
    suspend fun seedIfNeeded() {
        val alreadySeeded = settings.getBoolean(seedKey, false)
        if (alreadySeeded) return

        try {
            performSeed()
            settings.putBoolean(seedKey, true)
        } catch (e: Exception) {
            // If seeding fails (e.g. database error), log and continue.
            // The app will work without demo data — users can create accounts manually.
            e.printStackTrace()
        }
    }

    private suspend fun performSeed() {
        // 1. Create demo business
        val businessId = businessRepository.createBusiness(SeedData.demoBusiness)

        // 2. Create demo owner with password hash
        val ownerPasswordHash = PasswordHasher.hashPassword(SeedData.DEMO_OWNER_PASSWORD)
        val owner = SeedData.demoOwnerTemplate.copy(
            businessId = businessId,
            passwordHash = ownerPasswordHash
        )
        userRepository.createUser(owner)

        // 3. Create demo employee user (no password — logs in by code)
        val employeeUser = SeedData.demoEmployeeUserTemplate.copy(businessId = businessId)
        val employeeUserId = userRepository.createUser(employeeUser)

        // 4. Create demo employee record
        val employee = SeedData.demoEmployee.copy(
            businessId = businessId,
            userId = employeeUserId
        )
        employeeRepository.addEmployee(employee)

        // 5. Create demo client with password hash
        val clientPasswordHash = PasswordHasher.hashPassword(SeedData.DEMO_CLIENT_PASSWORD)
        val client = SeedData.demoClientTemplate.copy(passwordHash = clientPasswordHash)
        val clientUserId = userRepository.createUser(client)

        // 6. Create demo products (adjust businessId to actual)
        val productsWithBusinessId = SeedData.demoProducts.map { it.copy(businessId = businessId) }
        productsWithBusinessId.forEach { productRepository.addProduct(it) }

        // 7. Create 30 days of demo sales
        val sales = SeedData.buildDemoSales().map { it.copy(businessId = businessId) }
        sales.forEach { saleRepository.createSale(it) }

        // 8. Create budget goal for demo client
        val budgetGoal = SeedData.demoBudgetGoal.copy(clientUserId = clientUserId)
        budgetGoalRepository.upsert(budgetGoal)

        // 9. Create feedback reports
        val feedbackReports = SeedData.demoFeedbackReports.map {
            it.copy(clientUserId = clientUserId, targetBusinessId = businessId)
        }
        feedbackReports.forEach { feedbackReportRepository.submitReport(it) }

        // 10. Create shop rating
        val shopRating = SeedData.demoShopRating.copy(businessId = businessId)
        shopRatingRepository.upsert(shopRating)

        // 11. Create welcome chat message
        chatRepository.sendMessage(
            com.duka.shared.domain.ChatMessage(
                businessId = businessId,
                senderRole = "owner",
                senderLabel = "Owner",
                text = "Welcome to Duka! This is your demo shop."
            )
        )
    }
}
