package com.duka.shared.data.repository

import com.duka.shared.domain.*
import kotlinx.coroutines.flow.Flow

/**
 * Shared normalization for phoneOrEmail — trim + lowercase.
 */
fun normalizePhoneOrEmail(raw: String): String = raw.trim().lowercase()

/**
 * Repository interfaces — shared across Android and iOS.
 */
interface BusinessRepository {
    suspend fun createBusiness(business: Business): Long
    fun getActiveBusiness(): Flow<Business?>
    suspend fun getActiveBusinessOnce(): Business?
    suspend fun getById(id: Long): Business?
}

interface ProductRepository {
    suspend fun addProduct(product: Product): Long
    fun getProductsByBusiness(businessId: Long): Flow<List<Product>>
    fun getRecentProducts(businessId: Long): Flow<List<Product>>
    fun searchProducts(businessId: Long, query: String): Flow<List<Product>>
    suspend fun decrementStock(productId: Long): Boolean
    suspend fun incrementStock(productId: Long, amount: Int)
    suspend fun getProductById(id: Long): Product?
    suspend fun getProductsByIds(ids: List<Long>): List<Product>
    fun getCategoriesByBusiness(businessId: Long): Flow<List<String>>
    suspend fun updateProduct(product: Product)
    suspend fun softDelete(productId: Long)
    suspend fun getAllActiveByBusiness(businessId: Long): List<Product>
}

interface SaleRepository {
    suspend fun createSale(sale: Sale): Long
    suspend fun getSaleById(saleId: Long): Sale?
    fun getSalesInRange(businessId: Long, startTime: Long, endTime: Long): Flow<List<Sale>>
    fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Double?>
    suspend fun getTotalInRangeOnce(businessId: Long, startTime: Long, endTime: Long): Double?
    fun getSalesCountInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Int>
    fun getTopProducts(businessId: Long, startTime: Long, endTime: Long): Flow<List<ProductCount>>
    suspend fun getTotalAllTime(businessId: Long): Double?
    suspend fun getLatestSale(businessId: Long): Sale?
    suspend fun getAllByBusiness(businessId: Long): List<Sale>
    suspend fun getByProductInRange(productId: Long, startTime: Long, endTime: Long): List<Sale>
    suspend fun getAllTimeTopProducts(businessId: Long): List<ProductCount>
    suspend fun getSalesInRangeOnce(businessId: Long, startTime: Long, endTime: Long): List<Sale>
    suspend fun revenueByCategory(businessId: Long, from: Long, to: Long): List<CategoryRevenue>
    suspend fun revenueBySource(businessId: Long, from: Long, to: Long): List<SourceRevenue>
}

interface UserRepository {
    suspend fun createUser(user: User): Long
    suspend fun getById(id: Long): User?
    suspend fun getByPhoneOrEmail(phoneOrEmail: String): User?
    fun getEmployeesByBusiness(businessId: Long): Flow<List<User>>
    suspend fun getEmployeesByBusinessOnce(businessId: Long): List<User>
    suspend fun getEmployeeByName(businessId: Long, name: String): User?
    suspend fun updateName(userId: Long, name: String)
    suspend fun softDelete(userId: Long)
}

interface EmployeeRepository {
    suspend fun addEmployee(employee: Employee): Long
    fun getEmployeesByBusiness(businessId: Long): Flow<List<Employee>>
    suspend fun getEmployeesByBusinessOnce(businessId: Long): List<Employee>
    suspend fun getByCode(businessId: Long, code: String): Employee?
    suspend fun getActiveByCode(code: String): Employee?
    suspend fun getByCodeAny(code: String): Employee?
    suspend fun getById(id: Long): Employee?
    suspend fun updateRole(employeeId: Long, role: String)
    suspend fun updateActive(employeeId: Long, isActive: Boolean)
    suspend fun updateLastSeenAt(employeeId: Long, timestamp: Long)
    fun getActiveEmployeeCount(businessId: Long): Flow<Int>
    suspend fun deleteById(employeeId: Long)
}

interface ChatRepository {
    suspend fun sendMessage(message: ChatMessage): Long
    fun getMessages(businessId: Long): Flow<List<ChatMessage>>
    suspend fun deleteClientMessages(businessId: Long)
}

interface PromoRepository {
    suspend fun addPromo(promo: Promo): Long
    fun getPromosByBusiness(businessId: Long): Flow<List<Promo>>
    fun getLivePromos(businessId: Long): Flow<List<Promo>>
}

interface IssueRepository {
    suspend fun submitReport(report: IssueReport): Long
    fun getReportsByBusiness(businessId: Long): Flow<List<IssueReport>>
}

interface EbmRepository {
    suspend fun saveReceipt(receipt: EbmReceipt): Long
    suspend fun getBySaleId(saleId: Long): EbmReceipt?
    fun getReceiptsByBusiness(businessId: Long): Flow<List<EbmReceipt>>
}

interface TaxRepository {
    suspend fun upsert(taxProfile: TaxProfile)
    fun getTaxProfile(businessId: Long): Flow<TaxProfile?>
}

interface PurchaseRepository {
    suspend fun createPurchase(purchase: Purchase): Long
    fun getByClient(clientUserId: Long): Flow<List<Purchase>>
    suspend fun getTotalSpentInRange(clientUserId: Long, startTime: Long, endTime: Long): Double?
    suspend fun getTotalAllTime(clientUserId: Long): Double?
    suspend fun spendingByCategory(clientUserId: Long, from: Long, to: Long): List<CategoryRevenue>
    suspend fun spendingByBusiness(clientUserId: Long, from: Long, to: Long): List<BusinessSpend>
}

interface BudgetGoalRepository {
    suspend fun upsert(budgetGoal: BudgetGoal)
    fun getForClient(clientUserId: Long): Flow<BudgetGoal?>
    suspend fun getForClientOnce(clientUserId: Long): BudgetGoal?
}

interface FeedbackReportRepository {
    suspend fun submitReport(report: FeedbackReport): Long
    fun getByClient(clientUserId: Long): Flow<List<FeedbackReport>>
    fun getAll(): Flow<List<FeedbackReport>>
}

interface ShopRatingRepository {
    suspend fun upsert(rating: ShopRating)
    suspend fun getByBusiness(businessId: Long): ShopRating?
    fun getAllRanked(): Flow<List<ShopRating>>
}

interface WholesalerRepository {
    suspend fun addWholesaler(wholesaler: Wholesaler): Long
    fun getAll(): Flow<List<Wholesaler>>
    fun search(query: String): Flow<List<Wholesaler>>
    suspend fun getById(id: Long): Wholesaler?
}

interface RestockRequestRepository {
    suspend fun createRequest(request: RestockRequest): Long
    fun getByBusiness(businessId: Long): Flow<List<RestockRequest>>
    suspend fun updateStatus(requestId: Long, status: String)
}

interface StockAdjustmentRepository {
    suspend fun create(adjustment: StockAdjustment): Long
    fun getByBusiness(businessId: Long): Flow<List<StockAdjustment>>
    fun getByProduct(productId: Long): Flow<List<StockAdjustment>>
    suspend fun getUnitsSoldInRange(productId: Long, startTime: Long, endTime: Long): Int
}

interface ProductAlertRepository {
    suspend fun create(alert: ProductAlert): Long
    suspend fun createAll(alerts: List<ProductAlert>)
    fun getByBusiness(businessId: Long): Flow<List<ProductAlert>>
    fun getUnreadByBusiness(businessId: Long): Flow<List<ProductAlert>>
    fun getUnreadCount(businessId: Long): Flow<Int>
    fun getUnreadByProduct(businessId: Long, productId: Long): Flow<List<ProductAlert>>
    suspend fun getUnreadByProductOnce(businessId: Long, productId: Long): ProductAlert?
    suspend fun markAsRead(alertId: Long)
    suspend fun markAllAsRead(businessId: Long)
    suspend fun deleteById(alertId: Long)
}

interface AppNotificationRepository {
    suspend fun create(notification: AppNotification): Long
    fun getByUser(userId: Long): Flow<List<AppNotification>>
    fun getUnreadByUser(userId: Long): Flow<List<AppNotification>>
    fun getUnreadCount(userId: Long): Flow<Int>
    fun getByUserAndTypes(userId: Long, types: List<String>): Flow<List<AppNotification>>
    suspend fun markAsRead(notificationId: Long)
    suspend fun markAllAsRead(userId: Long)
    suspend fun deleteById(notificationId: Long)
}

interface ExpenseRepository {
    suspend fun create(expense: Expense): Long
    fun getByBusiness(businessId: Long): Flow<List<Expense>>
    suspend fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Long
    suspend fun deleteById(expenseId: Long)
}
