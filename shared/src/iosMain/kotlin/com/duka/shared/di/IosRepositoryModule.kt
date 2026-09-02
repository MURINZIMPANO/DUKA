package com.duka.shared.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.duka.shared.db.DukaDatabase
import com.duka.shared.data.repository.*
import com.duka.shared.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

/**
 * iOS repository module — provides shared repository implementations using SQLDelight.
 *
 * NOTE: These are minimal placeholder implementations that satisfy the shared interface contracts.
 * Full SQLDelight query implementations should be built when iOS development resumes on a Mac.
 * For now, the repositories return empty/default data so the app compiles and launches.
 */

// Placeholder implementations — replace with real SQLDelight queries on Mac

private class IosBusinessRepository : BusinessRepository {
    private val _activeBusiness = MutableStateFlow<Business?>(null)
    override suspend fun createBusiness(business: Business): Long = 1L
    override fun getActiveBusiness(): Flow<Business?> = _activeBusiness
    override suspend fun getActiveBusinessOnce(): Business? = null
    override suspend fun getById(id: Long): Business? = null
}

private class IosUserRepository : UserRepository {
    override suspend fun createUser(user: User): Long = 1L
    override suspend fun getById(id: Long): User? = null
    override suspend fun getByPhoneOrEmail(phoneOrEmail: String): User? = null
    override fun getEmployeesByBusiness(businessId: Long): Flow<List<User>> = flowOf(emptyList())
    override suspend fun getEmployeesByBusinessOnce(businessId: Long): List<User> = emptyList()
    override suspend fun getEmployeeByName(businessId: Long, name: String): User? = null
    override suspend fun updateName(userId: Long, name: String) {}
    override suspend fun softDelete(userId: Long) {}
}

private class IosProductRepository : ProductRepository {
    override suspend fun addProduct(product: Product): Long = 1L
    override fun getProductsByBusiness(businessId: Long): Flow<List<Product>> = flowOf(emptyList())
    override fun getRecentProducts(businessId: Long): Flow<List<Product>> = flowOf(emptyList())
    override fun searchProducts(businessId: Long, query: String): Flow<List<Product>> = flowOf(emptyList())
    override suspend fun decrementStock(productId: Long): Boolean = true
    override suspend fun incrementStock(productId: Long, amount: Int) {}
    override suspend fun getProductById(id: Long): Product? = null
    override suspend fun getProductsByIds(ids: List<Long>): List<Product> = emptyList()
    override fun getCategoriesByBusiness(businessId: Long): Flow<List<String>> = flowOf(emptyList())
    override suspend fun updateProduct(product: Product) {}
    override suspend fun softDelete(productId: Long) {}
    override suspend fun getAllActiveByBusiness(businessId: Long): List<Product> = emptyList()
}

private class IosSaleRepository : SaleRepository {
    override suspend fun createSale(sale: Sale): Long = 1L
    override suspend fun getSaleById(saleId: Long): Sale? = null
    override fun getSalesInRange(businessId: Long, startTime: Long, endTime: Long): Flow<List<Sale>> = flowOf(emptyList())
    override fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Double?> = flowOf(null)
    override suspend fun getTotalInRangeOnce(businessId: Long, startTime: Long, endTime: Long): Double? = null
    override fun getSalesCountInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Int> = flowOf(0)
    override fun getTopProducts(businessId: Long, startTime: Long, endTime: Long): Flow<List<ProductCount>> = flowOf(emptyList())
    override suspend fun getTotalAllTime(businessId: Long): Double? = null
    override suspend fun getLatestSale(businessId: Long): Sale? = null
    override suspend fun getAllByBusiness(businessId: Long): List<Sale> = emptyList()
    override suspend fun getByProductInRange(productId: Long, startTime: Long, endTime: Long): List<Sale> = emptyList()
    override suspend fun getAllTimeTopProducts(businessId: Long): List<ProductCount> = emptyList()
    override suspend fun getSalesInRangeOnce(businessId: Long, startTime: Long, endTime: Long): List<Sale> = emptyList()
    override suspend fun revenueByCategory(businessId: Long, from: Long, to: Long): List<CategoryRevenue> = emptyList()
    override suspend fun revenueBySource(businessId: Long, from: Long, to: Long): List<SourceRevenue> = emptyList()
}

private class IosEmployeeRepository : EmployeeRepository {
    override suspend fun addEmployee(employee: Employee): Long = 1L
    override fun getEmployeesByBusiness(businessId: Long): Flow<List<Employee>> = flowOf(emptyList())
    override suspend fun getEmployeesByBusinessOnce(businessId: Long): List<Employee> = emptyList()
    override suspend fun getByCode(businessId: Long, code: String): Employee? = null
    override suspend fun getActiveByCode(code: String): Employee? = null
    override suspend fun getByCodeAny(code: String): Employee? = null
    override suspend fun getById(id: Long): Employee? = null
    override suspend fun updateRole(employeeId: Long, role: String) {}
    override suspend fun updateActive(employeeId: Long, isActive: Boolean) {}
    override suspend fun updateLastSeenAt(employeeId: Long, timestamp: Long) {}
    override fun getActiveEmployeeCount(businessId: Long): Flow<Int> = flowOf(0)
    override suspend fun deleteById(employeeId: Long) {}
}

private class IosChatRepository : ChatRepository {
    override suspend fun sendMessage(message: ChatMessage): Long = 1L
    override fun getMessages(businessId: Long): Flow<List<ChatMessage>> = flowOf(emptyList())
    override suspend fun deleteClientMessages(businessId: Long) {}
}

private class IosPromoRepository : PromoRepository {
    override suspend fun addPromo(promo: Promo): Long = 1L
    override fun getPromosByBusiness(businessId: Long): Flow<List<Promo>> = flowOf(emptyList())
    override fun getLivePromos(businessId: Long): Flow<List<Promo>> = flowOf(emptyList())
}

private class IosIssueRepository : IssueRepository {
    override suspend fun submitReport(report: IssueReport): Long = 1L
    override fun getReportsByBusiness(businessId: Long): Flow<List<IssueReport>> = flowOf(emptyList())
}

private class IosEbmRepository : EbmRepository {
    override suspend fun saveReceipt(receipt: EbmReceipt): Long = 1L
    override suspend fun getBySaleId(saleId: Long): EbmReceipt? = null
    override fun getReceiptsByBusiness(businessId: Long): Flow<List<EbmReceipt>> = flowOf(emptyList())
}

private class IosTaxRepository : TaxRepository {
    override suspend fun upsert(taxProfile: TaxProfile) {}
    override fun getTaxProfile(businessId: Long): Flow<TaxProfile?> = flowOf(null)
}

private class IosPurchaseRepository : PurchaseRepository {
    override suspend fun createPurchase(purchase: Purchase): Long = 1L
    override fun getByClient(clientUserId: Long): Flow<List<Purchase>> = flowOf(emptyList())
    override suspend fun getTotalSpentInRange(clientUserId: Long, startTime: Long, endTime: Long): Double? = null
    override suspend fun getTotalAllTime(clientUserId: Long): Double? = null
    override suspend fun spendingByCategory(clientUserId: Long, from: Long, to: Long): List<CategoryRevenue> = emptyList()
    override suspend fun spendingByBusiness(clientUserId: Long, from: Long, to: Long): List<BusinessSpend> = emptyList()
}

private class IosBudgetGoalRepository : BudgetGoalRepository {
    override suspend fun upsert(budgetGoal: BudgetGoal) {}
    override fun getForClient(clientUserId: Long): Flow<BudgetGoal?> = flowOf(null)
    override suspend fun getForClientOnce(clientUserId: Long): BudgetGoal? = null
}

private class IosFeedbackReportRepository : FeedbackReportRepository {
    override suspend fun submitReport(report: FeedbackReport): Long = 1L
    override fun getByClient(clientUserId: Long): Flow<List<FeedbackReport>> = flowOf(emptyList())
    override fun getAll(): Flow<List<FeedbackReport>> = flowOf(emptyList())
}

private class IosShopRatingRepository : ShopRatingRepository {
    override suspend fun upsert(rating: ShopRating) {}
    override suspend fun getByBusiness(businessId: Long): ShopRating? = null
    override fun getAllRanked(): Flow<List<ShopRating>> = flowOf(emptyList())
}

private class IosWholesalerRepository : WholesalerRepository {
    override suspend fun addWholesaler(wholesaler: Wholesaler): Long = 1L
    override fun getAll(): Flow<List<Wholesaler>> = flowOf(emptyList())
    override fun search(query: String): Flow<List<Wholesaler>> = flowOf(emptyList())
    override suspend fun getById(id: Long): Wholesaler? = null
}

private class IosRestockRequestRepository : RestockRequestRepository {
    override suspend fun createRequest(request: RestockRequest): Long = 1L
    override fun getByBusiness(businessId: Long): Flow<List<RestockRequest>> = flowOf(emptyList())
    override suspend fun updateStatus(requestId: Long, status: String) {}
}

private class IosStockAdjustmentRepository : StockAdjustmentRepository {
    override suspend fun create(adjustment: StockAdjustment): Long = 1L
    override fun getByBusiness(businessId: Long): Flow<List<StockAdjustment>> = flowOf(emptyList())
    override fun getByProduct(productId: Long): Flow<List<StockAdjustment>> = flowOf(emptyList())
    override suspend fun getUnitsSoldInRange(productId: Long, startTime: Long, endTime: Long): Int = 0
}

private class IosProductAlertRepository : ProductAlertRepository {
    override suspend fun create(alert: ProductAlert): Long = 1L
    override suspend fun createAll(alerts: List<ProductAlert>) {}
    override fun getByBusiness(businessId: Long): Flow<List<ProductAlert>> = flowOf(emptyList())
    override fun getUnreadByBusiness(businessId: Long): Flow<List<ProductAlert>> = flowOf(emptyList())
    override fun getUnreadCount(businessId: Long): Flow<Int> = flowOf(0)
    override fun getUnreadByProduct(businessId: Long, productId: Long): Flow<List<ProductAlert>> = flowOf(emptyList())
    override suspend fun getUnreadByProductOnce(businessId: Long, productId: Long): ProductAlert? = null
    override suspend fun markAsRead(alertId: Long) {}
    override suspend fun markAllAsRead(businessId: Long) {}
    override suspend fun deleteById(alertId: Long) {}
}

private class IosAppNotificationRepository : AppNotificationRepository {
    override suspend fun create(notification: AppNotification): Long = 1L
    override fun getByUser(userId: Long): Flow<List<AppNotification>> = flowOf(emptyList())
    override fun getUnreadByUser(userId: Long): Flow<List<AppNotification>> = flowOf(emptyList())
    override fun getUnreadCount(userId: Long): Flow<Int> = flowOf(0)
    override fun getByUserAndTypes(userId: Long, types: List<String>): Flow<List<AppNotification>> = flowOf(emptyList())
    override suspend fun markAsRead(notificationId: Long) {}
    override suspend fun markAllAsRead(userId: Long) {}
    override suspend fun deleteById(notificationId: Long) {}
}

private class IosExpenseRepository : ExpenseRepository {
    override suspend fun create(expense: Expense): Long = 1L
    override fun getByBusiness(businessId: Long): Flow<List<Expense>> = flowOf(emptyList())
    override suspend fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Long = 0L
    override suspend fun deleteById(expenseId: Long) {}
}

/**
 * iOS Koin module — provides shared repository implementations for iOS.
 *
 * TODO: Replace these placeholder implementations with real SQLDelight-backed
 * repositories when iOS development resumes on a Mac with Xcode.
 */
val iosRepositoryModule = module {
    single<BusinessRepository> { IosBusinessRepository() }
    single<UserRepository> { IosUserRepository() }
    single<ProductRepository> { IosProductRepository() }
    single<SaleRepository> { IosSaleRepository() }
    single<EmployeeRepository> { IosEmployeeRepository() }
    single<ChatRepository> { IosChatRepository() }
    single<PromoRepository> { IosPromoRepository() }
    single<IssueRepository> { IosIssueRepository() }
    single<EbmRepository> { IosEbmRepository() }
    single<TaxRepository> { IosTaxRepository() }
    single<PurchaseRepository> { IosPurchaseRepository() }
    single<BudgetGoalRepository> { IosBudgetGoalRepository() }
    single<FeedbackReportRepository> { IosFeedbackReportRepository() }
    single<ShopRatingRepository> { IosShopRatingRepository() }
    single<WholesalerRepository> { IosWholesalerRepository() }
    single<RestockRequestRepository> { IosRestockRequestRepository() }
    single<StockAdjustmentRepository> { IosStockAdjustmentRepository() }
    single<ProductAlertRepository> { IosProductAlertRepository() }
    single<AppNotificationRepository> { IosAppNotificationRepository() }
    single<ExpenseRepository> { IosExpenseRepository() }
}
