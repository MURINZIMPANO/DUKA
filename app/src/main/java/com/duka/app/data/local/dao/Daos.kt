package com.duka.app.data.local.dao

import androidx.room.*
import com.duka.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): User?

    @Query("SELECT * FROM users WHERE phoneOrEmail = :phoneOrEmail AND isDeleted = 0 LIMIT 1")
    suspend fun getByPhoneOrEmail(phoneOrEmail: String): User?

    @Query("SELECT * FROM users WHERE businessId = :businessId AND role = 'employee'")
    fun getEmployeesByBusiness(businessId: Long): Flow<List<User>>

    @Query("SELECT * FROM users WHERE businessId = :businessId AND role = 'employee'")
    suspend fun getEmployeesByBusinessOnce(businessId: Long): List<User>

    @Query("SELECT * FROM users WHERE businessId = :businessId AND role = 'employee' AND fullName = :name LIMIT 1")
    suspend fun getEmployeeByName(businessId: Long, name: String): User?

    @Query("UPDATE users SET fullName = :name WHERE id = :userId")
    suspend fun updateName(userId: Long, name: String)

    @Query("UPDATE users SET isDeleted = 1 WHERE id = :userId")
    suspend fun softDelete(userId: Long)
}

@Dao
interface BusinessDao {
    @Insert
    suspend fun insert(business: Business): Long

    @Query("SELECT * FROM businesses WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Business?

    @Query("SELECT * FROM businesses LIMIT 1")
    fun getActiveBusiness(): Flow<Business?>

    @Query("SELECT * FROM businesses LIMIT 1")
    suspend fun getActiveBusinessOnce(): Business?
}

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product): Long

    @Update
    suspend fun update(product: Product)

    @Query("SELECT * FROM products WHERE businessId = :businessId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getProductsByBusiness(businessId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE businessId = :businessId AND isDeleted = 0 ORDER BY createdAt DESC LIMIT 5")
    fun getRecentProducts(businessId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE businessId = :businessId AND isDeleted = 0 AND name LIKE '%' || :query || '%'")
    fun searchProducts(businessId: Long, query: String): Flow<List<Product>>

    @Query("UPDATE products SET stockQuantity = stockQuantity - 1 WHERE id = :productId AND stockQuantity > 0")
    suspend fun decrementStock(productId: Long): Int

    @Query("UPDATE products SET stockQuantity = stockQuantity + :amount WHERE id = :productId")
    suspend fun incrementStock(productId: Long, amount: Int)

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Query("SELECT * FROM products WHERE id IN (:ids)")
    suspend fun getProductsByIds(ids: List<Long>): List<Product>

    @Query("SELECT DISTINCT category FROM products WHERE businessId = :businessId AND isDeleted = 0 ORDER BY category")
    fun getCategoriesByBusiness(businessId: Long): Flow<List<String>>

    @Query("SELECT * FROM products WHERE businessId = :businessId AND isDeleted = 0")
    suspend fun getAllActiveByBusiness(businessId: Long): List<Product>

    @Query("UPDATE products SET isDeleted = 1 WHERE id = :productId")
    suspend fun softDelete(productId: Long)
}

@Dao
interface SaleDao {
    @Insert
    suspend fun insert(sale: Sale): Long

    @Query("SELECT * FROM sales WHERE id = :saleId")
    suspend fun getSaleById(saleId: Long): Sale?

    @Query("SELECT * FROM sales WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime")
    fun getSalesInRange(businessId: Long, startTime: Long, endTime: Long): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getSalesInRangeOnce(businessId: Long, startTime: Long, endTime: Long): List<Sale>

    @Query("SELECT SUM(amount) FROM sales WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime")
    fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM sales WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getTotalInRangeOnce(businessId: Long, startTime: Long, endTime: Long): Double?

    @Query("SELECT COUNT(*) FROM sales WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime")
    fun getSalesCountInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Int>

    @Query("""
        SELECT productId, COUNT(*) as cnt FROM sales 
        WHERE businessId = :businessId AND timestamp >= :startTime AND timestamp < :endTime
        GROUP BY productId ORDER BY cnt DESC
    """)
    fun getTopProducts(businessId: Long, startTime: Long, endTime: Long): Flow<List<ProductCount>>

    @Query("SELECT SUM(amount) FROM sales WHERE businessId = :businessId")
    suspend fun getTotalAllTime(businessId: Long): Double?

    @Query("SELECT * FROM sales WHERE businessId = :businessId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestSale(businessId: Long): Sale?

    // V5: Analytics queries
    @Query("SELECT * FROM sales WHERE businessId = :businessId")
    suspend fun getAllByBusiness(businessId: Long): List<Sale>

    @Query("SELECT * FROM sales WHERE productId = :productId AND timestamp >= :startTime AND timestamp < :endTime")
    suspend fun getByProductInRange(productId: Long, startTime: Long, endTime: Long): List<Sale>

    @Query("SELECT productId, COUNT(*) as cnt FROM sales WHERE businessId = :businessId GROUP BY productId ORDER BY cnt DESC")
    suspend fun getAllTimeTopProducts(businessId: Long): List<ProductCount>

    @Query("""
        SELECT p.category, SUM(s.amount) as total
        FROM sales s
        INNER JOIN products p ON s.productId = p.id
        WHERE s.businessId = :businessId
          AND s.timestamp BETWEEN :from AND :to
        GROUP BY p.category
    """)
    suspend fun revenueByCategory(businessId: Long, from: Long, to: Long): List<CategoryRevenue>

    @Query("""
        SELECT source, SUM(amount) as total
        FROM sales
        WHERE businessId = :businessId
          AND timestamp BETWEEN :from AND :to
        GROUP BY source
    """)
    suspend fun revenueBySource(businessId: Long, from: Long, to: Long): List<SourceRevenue>
}

data class ProductCount(
    val productId: Long,
    val cnt: Int
)

/**
 * Revenue by category — used for Owner pie chart.
 * Joins sales with products to group by category.
 */
data class CategoryRevenue(
    val category: String,
    val total: Double
)

/**
 * Revenue by source — used for Owner pie chart.
 */
data class SourceRevenue(
    val source: String,
    val total: Double
)

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert(employee: Employee): Long

    @Query("SELECT * FROM employees WHERE businessId = :businessId")
    fun getEmployeesByBusiness(businessId: Long): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE businessId = :businessId")
    suspend fun getEmployeesByBusinessOnce(businessId: Long): List<Employee>

    @Query("SELECT * FROM employees WHERE businessId = :businessId AND code = :code LIMIT 1")
    suspend fun getByCode(businessId: Long, code: String): Employee?

    @Query("SELECT * FROM employees WHERE code = :code AND isActive = 1 LIMIT 1")
    suspend fun getActiveByCode(code: String): Employee?

    @Query("SELECT * FROM employees WHERE code = :code LIMIT 1")
    suspend fun getByCodeAny(code: String): Employee?

    @Query("SELECT * FROM employees WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Employee?

    @Query("UPDATE employees SET role = :role WHERE id = :employeeId")
    suspend fun updateRole(employeeId: Long, role: String)

    @Query("UPDATE employees SET isActive = :isActive WHERE id = :employeeId")
    suspend fun updateActive(employeeId: Long, isActive: Boolean)

    @Query("UPDATE employees SET lastSeenAt = :timestamp WHERE id = :employeeId")
    suspend fun updateLastSeenAt(employeeId: Long, timestamp: Long)

    @Query("SELECT COUNT(*) FROM employees WHERE businessId = :businessId AND isActive = 1")
    fun getActiveEmployeeCount(businessId: Long): Flow<Int>

    @Query("DELETE FROM employees WHERE id = :employeeId")
    suspend fun deleteById(employeeId: Long)
}

@Dao
interface ChatMessageDao {
    @Insert
    suspend fun insert(message: ChatMessage): Long

    @Query("SELECT * FROM chat_messages WHERE businessId = :businessId ORDER BY timestamp ASC")
    fun getMessagesByBusiness(businessId: Long): Flow<List<ChatMessage>>

    @Query("DELETE FROM chat_messages WHERE businessId = :businessId AND senderRole = 'client'")
    suspend fun deleteClientMessages(businessId: Long)
}

@Dao
interface PromoDao {
    @Insert
    suspend fun insert(promo: Promo): Long

    @Query("SELECT * FROM promos WHERE businessId = :businessId ORDER BY startDate DESC")
    fun getPromosByBusiness(businessId: Long): Flow<List<Promo>>

    @Query("SELECT * FROM promos WHERE businessId = :businessId AND isLive = 1")
    fun getLivePromos(businessId: Long): Flow<List<Promo>>
}

@Dao
interface IssueReportDao {
    @Insert
    suspend fun insert(report: IssueReport): Long

    @Query("SELECT * FROM issue_reports WHERE businessId = :businessId ORDER BY createdAt DESC")
    fun getReportsByBusiness(businessId: Long): Flow<List<IssueReport>>
}

@Dao
interface EbmReceiptDao {
    @Insert
    suspend fun insert(receipt: EbmReceipt): Long

    @Query("SELECT * FROM ebm_receipts WHERE saleId = :saleId")
    suspend fun getBySaleId(saleId: Long): EbmReceipt?

    @Query("""
        SELECT e.* FROM ebm_receipts e 
        JOIN sales s ON e.saleId = s.id 
        WHERE s.businessId = :businessId 
        ORDER BY e.sentAt DESC
    """)
    fun getReceiptsByBusiness(businessId: Long): Flow<List<EbmReceipt>>
}

@Dao
interface TaxProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(taxProfile: TaxProfile)

    @Query("SELECT * FROM tax_profiles WHERE businessId = :businessId")
    fun getTaxProfile(businessId: Long): Flow<TaxProfile?>
}
