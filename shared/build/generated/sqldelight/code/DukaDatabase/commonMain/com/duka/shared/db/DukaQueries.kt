package com.duka.shared.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.Collection

public class DukaQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllBusinesses(mapper: (
    id: Long,
    name: String,
    type: String,
    employeeCount: Long,
    language: String,
    district: String,
    createdAt: Long,
  ) -> T): Query<T> = Query(-792_372_367, arrayOf("businesses"), driver, "Duka.sq",
      "selectAllBusinesses",
      "SELECT businesses.id, businesses.name, businesses.type, businesses.employeeCount, businesses.language, businesses.district, businesses.createdAt FROM businesses LIMIT 1") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectAllBusinesses(): Query<Businesses> = selectAllBusinesses { id, name, type,
      employeeCount, language, district, createdAt ->
    Businesses(
      id,
      name,
      type,
      employeeCount,
      language,
      district,
      createdAt
    )
  }

  public fun <T : Any> selectBusinessById(id: Long, mapper: (
    id: Long,
    name: String,
    type: String,
    employeeCount: Long,
    language: String,
    district: String,
    createdAt: Long,
  ) -> T): Query<T> = SelectBusinessByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectBusinessById(id: Long): Query<Businesses> = selectBusinessById(id) { id_, name,
      type, employeeCount, language, district, createdAt ->
    Businesses(
      id_,
      name,
      type,
      employeeCount,
      language,
      district,
      createdAt
    )
  }

  public fun <T : Any> selectUserById(id: Long, mapper: (
    id: Long,
    businessId: Long?,
    fullName: String,
    phoneOrEmail: String,
    passwordHash: String,
    role: String,
    isDeleted: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectUserByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1),
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectUserById(id: Long): Query<Users> = selectUserById(id) { id_, businessId,
      fullName, phoneOrEmail, passwordHash, role, isDeleted, createdAt ->
    Users(
      id_,
      businessId,
      fullName,
      phoneOrEmail,
      passwordHash,
      role,
      isDeleted,
      createdAt
    )
  }

  public fun <T : Any> selectUserByPhoneOrEmail(phoneOrEmail: String, mapper: (
    id: Long,
    businessId: Long?,
    fullName: String,
    phoneOrEmail: String,
    passwordHash: String,
    role: String,
    isDeleted: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectUserByPhoneOrEmailQuery(phoneOrEmail) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1),
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectUserByPhoneOrEmail(phoneOrEmail: String): Query<Users> =
      selectUserByPhoneOrEmail(phoneOrEmail) { id, businessId, fullName, phoneOrEmail_,
      passwordHash, role, isDeleted, createdAt ->
    Users(
      id,
      businessId,
      fullName,
      phoneOrEmail_,
      passwordHash,
      role,
      isDeleted,
      createdAt
    )
  }

  public fun <T : Any> selectEmployeesByBusiness(businessId: Long?, mapper: (
    id: Long,
    businessId: Long?,
    fullName: String,
    phoneOrEmail: String,
    passwordHash: String,
    role: String,
    isDeleted: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectEmployeesByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1),
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectEmployeesByBusiness(businessId: Long?): Query<Users> =
      selectEmployeesByBusiness(businessId) { id, businessId_, fullName, phoneOrEmail, passwordHash,
      role, isDeleted, createdAt ->
    Users(
      id,
      businessId_,
      fullName,
      phoneOrEmail,
      passwordHash,
      role,
      isDeleted,
      createdAt
    )
  }

  public fun <T : Any> selectEmployeeByName(
    businessId: Long?,
    fullName: String,
    mapper: (
      id: Long,
      businessId: Long?,
      fullName: String,
      phoneOrEmail: String,
      passwordHash: String,
      role: String,
      isDeleted: Long,
      createdAt: Long,
    ) -> T,
  ): Query<T> = SelectEmployeeByNameQuery(businessId, fullName) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1),
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectEmployeeByName(businessId: Long?, fullName: String): Query<Users> =
      selectEmployeeByName(businessId, fullName) { id, businessId_, fullName_, phoneOrEmail,
      passwordHash, role, isDeleted, createdAt ->
    Users(
      id,
      businessId_,
      fullName_,
      phoneOrEmail,
      passwordHash,
      role,
      isDeleted,
      createdAt
    )
  }

  public fun <T : Any> selectProductsByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    createdAt: Long,
    isDeleted: Long,
    costPrice: Long,
    lowStockThreshold: Long,
  ) -> T): Query<T> = SelectProductsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!
    )
  }

  public fun selectProductsByBusiness(businessId: Long): Query<Products> =
      selectProductsByBusiness(businessId) { id, businessId_, name, price, stockQuantity, category,
      createdAt, isDeleted, costPrice, lowStockThreshold ->
    Products(
      id,
      businessId_,
      name,
      price,
      stockQuantity,
      category,
      createdAt,
      isDeleted,
      costPrice,
      lowStockThreshold
    )
  }

  public fun <T : Any> selectRecentProducts(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    createdAt: Long,
    isDeleted: Long,
    costPrice: Long,
    lowStockThreshold: Long,
  ) -> T): Query<T> = SelectRecentProductsQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!
    )
  }

  public fun selectRecentProducts(businessId: Long): Query<Products> =
      selectRecentProducts(businessId) { id, businessId_, name, price, stockQuantity, category,
      createdAt, isDeleted, costPrice, lowStockThreshold ->
    Products(
      id,
      businessId_,
      name,
      price,
      stockQuantity,
      category,
      createdAt,
      isDeleted,
      costPrice,
      lowStockThreshold
    )
  }

  public fun <T : Any> selectProductById(id: Long, mapper: (
    id: Long,
    businessId: Long,
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    createdAt: Long,
    isDeleted: Long,
    costPrice: Long,
    lowStockThreshold: Long,
  ) -> T): Query<T> = SelectProductByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!
    )
  }

  public fun selectProductById(id: Long): Query<Products> = selectProductById(id) { id_, businessId,
      name, price, stockQuantity, category, createdAt, isDeleted, costPrice, lowStockThreshold ->
    Products(
      id_,
      businessId,
      name,
      price,
      stockQuantity,
      category,
      createdAt,
      isDeleted,
      costPrice,
      lowStockThreshold
    )
  }

  public fun <T : Any> selectProductsByIds(id: Collection<Long>, mapper: (
    id: Long,
    businessId: Long,
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    createdAt: Long,
    isDeleted: Long,
    costPrice: Long,
    lowStockThreshold: Long,
  ) -> T): Query<T> = SelectProductsByIdsQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!
    )
  }

  public fun selectProductsByIds(id: Collection<Long>): Query<Products> = selectProductsByIds(id) {
      id_, businessId, name, price, stockQuantity, category, createdAt, isDeleted, costPrice,
      lowStockThreshold ->
    Products(
      id_,
      businessId,
      name,
      price,
      stockQuantity,
      category,
      createdAt,
      isDeleted,
      costPrice,
      lowStockThreshold
    )
  }

  public fun selectCategoriesByBusiness(businessId: Long): Query<String> =
      SelectCategoriesByBusinessQuery(businessId) { cursor ->
    cursor.getString(0)!!
  }

  public fun <T : Any> selectAllActiveByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    createdAt: Long,
    isDeleted: Long,
    costPrice: Long,
    lowStockThreshold: Long,
  ) -> T): Query<T> = SelectAllActiveByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!,
      cursor.getLong(9)!!
    )
  }

  public fun selectAllActiveByBusiness(businessId: Long): Query<Products> =
      selectAllActiveByBusiness(businessId) { id, businessId_, name, price, stockQuantity, category,
      createdAt, isDeleted, costPrice, lowStockThreshold ->
    Products(
      id,
      businessId_,
      name,
      price,
      stockQuantity,
      category,
      createdAt,
      isDeleted,
      costPrice,
      lowStockThreshold
    )
  }

  public fun <T : Any> selectSaleById(id: Long, mapper: (
    id: Long,
    productId: Long,
    businessId: Long,
    amount: Double,
    timestamp: Long,
    source: String,
  ) -> T): Query<T> = SelectSaleByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!
    )
  }

  public fun selectSaleById(id: Long): Query<Sales> = selectSaleById(id) { id_, productId,
      businessId, amount, timestamp, source ->
    Sales(
      id_,
      productId,
      businessId,
      amount,
      timestamp,
      source
    )
  }

  public fun <T : Any> selectSalesInRange(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (
      id: Long,
      productId: Long,
      businessId: Long,
      amount: Double,
      timestamp: Long,
      source: String,
    ) -> T,
  ): Query<T> = SelectSalesInRangeQuery(businessId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!
    )
  }

  public fun selectSalesInRange(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<Sales> = selectSalesInRange(businessId, timestamp, timestamp_) { id, productId,
      businessId_, amount, timestamp__, source ->
    Sales(
      id,
      productId,
      businessId_,
      amount,
      timestamp__,
      source
    )
  }

  public fun <T : Any> selectTotalInRange(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (SUM: Double?) -> T,
  ): Query<T> = SelectTotalInRangeQuery(businessId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getDouble(0)
    )
  }

  public fun selectTotalInRange(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectTotalInRange> = selectTotalInRange(businessId, timestamp, timestamp_) { SUM ->
    SelectTotalInRange(
      SUM
    )
  }

  public fun selectSalesCountInRange(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<Long> = SelectSalesCountInRangeQuery(businessId, timestamp, timestamp_) { cursor ->
    cursor.getLong(0)!!
  }

  public fun <T : Any> selectTotalAllTime(businessId: Long, mapper: (SUM: Double?) -> T): Query<T> =
      SelectTotalAllTimeQuery(businessId) { cursor ->
    mapper(
      cursor.getDouble(0)
    )
  }

  public fun selectTotalAllTime(businessId: Long): Query<SelectTotalAllTime> =
      selectTotalAllTime(businessId) { SUM ->
    SelectTotalAllTime(
      SUM
    )
  }

  public fun <T : Any> selectLatestSale(businessId: Long, mapper: (
    id: Long,
    productId: Long,
    businessId: Long,
    amount: Double,
    timestamp: Long,
    source: String,
  ) -> T): Query<T> = SelectLatestSaleQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!
    )
  }

  public fun selectLatestSale(businessId: Long): Query<Sales> = selectLatestSale(businessId) { id,
      productId, businessId_, amount, timestamp, source ->
    Sales(
      id,
      productId,
      businessId_,
      amount,
      timestamp,
      source
    )
  }

  public fun <T : Any> selectAllByBusiness(businessId: Long, mapper: (
    id: Long,
    productId: Long,
    businessId: Long,
    amount: Double,
    timestamp: Long,
    source: String,
  ) -> T): Query<T> = SelectAllByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getDouble(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!
    )
  }

  public fun selectAllByBusiness(businessId: Long): Query<Sales> = selectAllByBusiness(businessId) {
      id, productId, businessId_, amount, timestamp, source ->
    Sales(
      id,
      productId,
      businessId_,
      amount,
      timestamp,
      source
    )
  }

  public fun <T : Any> selectAllTimeTopProducts(businessId: Long, mapper: (productId: Long,
      cnt: Long) -> T): Query<T> = SelectAllTimeTopProductsQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  public fun selectAllTimeTopProducts(businessId: Long): Query<SelectAllTimeTopProducts> =
      selectAllTimeTopProducts(businessId) { productId, cnt ->
    SelectAllTimeTopProducts(
      productId,
      cnt
    )
  }

  public fun <T : Any> selectRevenueByCategory(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (category: String, total: Double?) -> T,
  ): Query<T> = SelectRevenueByCategoryQuery(businessId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getDouble(1)
    )
  }

  public fun selectRevenueByCategory(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectRevenueByCategory> = selectRevenueByCategory(businessId, timestamp, timestamp_) {
      category, total ->
    SelectRevenueByCategory(
      category,
      total
    )
  }

  public fun <T : Any> selectRevenueBySource(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (source: String, total: Double?) -> T,
  ): Query<T> = SelectRevenueBySourceQuery(businessId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getDouble(1)
    )
  }

  public fun selectRevenueBySource(
    businessId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectRevenueBySource> = selectRevenueBySource(businessId, timestamp, timestamp_) {
      source, total ->
    SelectRevenueBySource(
      source,
      total
    )
  }

  public fun <T : Any> selectEmployeesByBusinessEmp(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    userId: Long,
    code: String,
    name: String,
    role: String,
    isActive: Long,
    joinedAt: Long,
    lastSeenAt: Long?,
  ) -> T): Query<T> = SelectEmployeesByBusinessEmpQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)
    )
  }

  public fun selectEmployeesByBusinessEmp(businessId: Long): Query<Employees> =
      selectEmployeesByBusinessEmp(businessId) { id, businessId_, userId, code, name, role,
      isActive, joinedAt, lastSeenAt ->
    Employees(
      id,
      businessId_,
      userId,
      code,
      name,
      role,
      isActive,
      joinedAt,
      lastSeenAt
    )
  }

  public fun <T : Any> selectEmployeeByCode(
    businessId: Long,
    code: String,
    mapper: (
      id: Long,
      businessId: Long,
      userId: Long,
      code: String,
      name: String,
      role: String,
      isActive: Long,
      joinedAt: Long,
      lastSeenAt: Long?,
    ) -> T,
  ): Query<T> = SelectEmployeeByCodeQuery(businessId, code) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)
    )
  }

  public fun selectEmployeeByCode(businessId: Long, code: String): Query<Employees> =
      selectEmployeeByCode(businessId, code) { id, businessId_, userId, code_, name, role, isActive,
      joinedAt, lastSeenAt ->
    Employees(
      id,
      businessId_,
      userId,
      code_,
      name,
      role,
      isActive,
      joinedAt,
      lastSeenAt
    )
  }

  public fun <T : Any> selectActiveEmployeeByCode(code: String, mapper: (
    id: Long,
    businessId: Long,
    userId: Long,
    code: String,
    name: String,
    role: String,
    isActive: Long,
    joinedAt: Long,
    lastSeenAt: Long?,
  ) -> T): Query<T> = SelectActiveEmployeeByCodeQuery(code) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)
    )
  }

  public fun selectActiveEmployeeByCode(code: String): Query<Employees> =
      selectActiveEmployeeByCode(code) { id, businessId, userId, code_, name, role, isActive,
      joinedAt, lastSeenAt ->
    Employees(
      id,
      businessId,
      userId,
      code_,
      name,
      role,
      isActive,
      joinedAt,
      lastSeenAt
    )
  }

  public fun <T : Any> selectEmployeeByCodeAny(code: String, mapper: (
    id: Long,
    businessId: Long,
    userId: Long,
    code: String,
    name: String,
    role: String,
    isActive: Long,
    joinedAt: Long,
    lastSeenAt: Long?,
  ) -> T): Query<T> = SelectEmployeeByCodeAnyQuery(code) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)
    )
  }

  public fun selectEmployeeByCodeAny(code: String): Query<Employees> =
      selectEmployeeByCodeAny(code) { id, businessId, userId, code_, name, role, isActive, joinedAt,
      lastSeenAt ->
    Employees(
      id,
      businessId,
      userId,
      code_,
      name,
      role,
      isActive,
      joinedAt,
      lastSeenAt
    )
  }

  public fun <T : Any> selectEmployeeById(id: Long, mapper: (
    id: Long,
    businessId: Long,
    userId: Long,
    code: String,
    name: String,
    role: String,
    isActive: Long,
    joinedAt: Long,
    lastSeenAt: Long?,
  ) -> T): Query<T> = SelectEmployeeByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)
    )
  }

  public fun selectEmployeeById(id: Long): Query<Employees> = selectEmployeeById(id) { id_,
      businessId, userId, code, name, role, isActive, joinedAt, lastSeenAt ->
    Employees(
      id_,
      businessId,
      userId,
      code,
      name,
      role,
      isActive,
      joinedAt,
      lastSeenAt
    )
  }

  public fun selectActiveEmployeeCount(businessId: Long): Query<Long> =
      SelectActiveEmployeeCountQuery(businessId) { cursor ->
    cursor.getLong(0)!!
  }

  public fun <T : Any> selectChatMessages(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    senderRole: String,
    senderLabel: String,
    text: String,
    timestamp: Long,
  ) -> T): Query<T> = SelectChatMessagesQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectChatMessages(businessId: Long): Query<Chat_messages> =
      selectChatMessages(businessId) { id, businessId_, senderRole, senderLabel, text, timestamp ->
    Chat_messages(
      id,
      businessId_,
      senderRole,
      senderLabel,
      text,
      timestamp
    )
  }

  public fun <T : Any> selectPromosByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    title: String,
    discountPercent: Long,
    startDate: Long,
    endDate: Long,
    isLive: Long,
  ) -> T): Query<T> = SelectPromosByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectPromosByBusiness(businessId: Long): Query<Promos> =
      selectPromosByBusiness(businessId) { id, businessId_, title, discountPercent, startDate,
      endDate, isLive ->
    Promos(
      id,
      businessId_,
      title,
      discountPercent,
      startDate,
      endDate,
      isLive
    )
  }

  public fun <T : Any> selectLivePromos(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    title: String,
    discountPercent: Long,
    startDate: Long,
    endDate: Long,
    isLive: Long,
  ) -> T): Query<T> = SelectLivePromosQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectLivePromos(businessId: Long): Query<Promos> = selectLivePromos(businessId) { id,
      businessId_, title, discountPercent, startDate, endDate, isLive ->
    Promos(
      id,
      businessId_,
      title,
      discountPercent,
      startDate,
      endDate,
      isLive
    )
  }

  public fun <T : Any> selectReportsByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    category: String,
    message: String,
    attachBusinessId: Long,
    status: String,
    createdAt: Long,
  ) -> T): Query<T> = SelectReportsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectReportsByBusiness(businessId: Long): Query<Issue_reports> =
      selectReportsByBusiness(businessId) { id, businessId_, category, message, attachBusinessId,
      status, createdAt ->
    Issue_reports(
      id,
      businessId_,
      category,
      message,
      attachBusinessId,
      status,
      createdAt
    )
  }

  public fun <T : Any> selectReceiptBySaleId(saleId: Long, mapper: (
    id: Long,
    saleId: Long,
    receiptNumber: String,
    status: String,
    sentAt: Long,
  ) -> T): Query<T> = SelectReceiptBySaleIdQuery(saleId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun selectReceiptBySaleId(saleId: Long): Query<Ebm_receipts> =
      selectReceiptBySaleId(saleId) { id, saleId_, receiptNumber, status, sentAt ->
    Ebm_receipts(
      id,
      saleId_,
      receiptNumber,
      status,
      sentAt
    )
  }

  public fun <T : Any> selectReceiptsByBusiness(businessId: Long, mapper: (
    id: Long,
    saleId: Long,
    receiptNumber: String,
    status: String,
    sentAt: Long,
  ) -> T): Query<T> = SelectReceiptsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!
    )
  }

  public fun selectReceiptsByBusiness(businessId: Long): Query<Ebm_receipts> =
      selectReceiptsByBusiness(businessId) { id, saleId, receiptNumber, status, sentAt ->
    Ebm_receipts(
      id,
      saleId,
      receiptNumber,
      status,
      sentAt
    )
  }

  public fun <T : Any> selectTaxProfile(businessId: Long, mapper: (
    businessId: Long,
    quarterlyTurnover: Double,
    tier: String,
    vatCollected: Double,
  ) -> T): Query<T> = SelectTaxProfileQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getDouble(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!
    )
  }

  public fun selectTaxProfile(businessId: Long): Query<Tax_profiles> =
      selectTaxProfile(businessId) { businessId_, quarterlyTurnover, tier, vatCollected ->
    Tax_profiles(
      businessId_,
      quarterlyTurnover,
      tier,
      vatCollected
    )
  }

  public fun <T : Any> selectPurchasesByClient(clientUserId: Long, mapper: (
    id: Long,
    clientUserId: Long,
    businessId: Long,
    productId: Long,
    amount: Double,
    timestamp: Long,
    receiptNumber: String,
  ) -> T): Query<T> = SelectPurchasesByClientQuery(clientUserId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!,
      cursor.getDouble(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!
    )
  }

  public fun selectPurchasesByClient(clientUserId: Long): Query<Purchases> =
      selectPurchasesByClient(clientUserId) { id, clientUserId_, businessId, productId, amount,
      timestamp, receiptNumber ->
    Purchases(
      id,
      clientUserId_,
      businessId,
      productId,
      amount,
      timestamp,
      receiptNumber
    )
  }

  public fun <T : Any> selectTotalSpentInRange(
    clientUserId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (SUM: Double?) -> T,
  ): Query<T> = SelectTotalSpentInRangeQuery(clientUserId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getDouble(0)
    )
  }

  public fun selectTotalSpentInRange(
    clientUserId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectTotalSpentInRange> = selectTotalSpentInRange(clientUserId, timestamp, timestamp_) {
      SUM ->
    SelectTotalSpentInRange(
      SUM
    )
  }

  public fun <T : Any> selectTotalAllTimeClient(clientUserId: Long, mapper: (SUM: Double?) -> T):
      Query<T> = SelectTotalAllTimeClientQuery(clientUserId) { cursor ->
    mapper(
      cursor.getDouble(0)
    )
  }

  public fun selectTotalAllTimeClient(clientUserId: Long): Query<SelectTotalAllTimeClient> =
      selectTotalAllTimeClient(clientUserId) { SUM ->
    SelectTotalAllTimeClient(
      SUM
    )
  }

  public fun <T : Any> selectSpendingByCategory(
    clientUserId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (category: String, total: Double?) -> T,
  ): Query<T> = SelectSpendingByCategoryQuery(clientUserId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getDouble(1)
    )
  }

  public fun selectSpendingByCategory(
    clientUserId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectSpendingByCategory> = selectSpendingByCategory(clientUserId, timestamp,
      timestamp_) { category, total ->
    SelectSpendingByCategory(
      category,
      total
    )
  }

  public fun <T : Any> selectSpendingByBusiness(
    clientUserId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (businessId: Long, total: Double?) -> T,
  ): Query<T> = SelectSpendingByBusinessQuery(clientUserId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getDouble(1)
    )
  }

  public fun selectSpendingByBusiness(
    clientUserId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectSpendingByBusiness> = selectSpendingByBusiness(clientUserId, timestamp,
      timestamp_) { businessId, total ->
    SelectSpendingByBusiness(
      businessId,
      total
    )
  }

  public fun <T : Any> selectBudgetGoalForClient(clientUserId: Long, mapper: (
    id: Long,
    clientUserId: Long,
    weeklyLimit: Double,
    createdAt: Long,
  ) -> T): Query<T> = SelectBudgetGoalForClientQuery(clientUserId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getDouble(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun selectBudgetGoalForClient(clientUserId: Long): Query<Budget_goals> =
      selectBudgetGoalForClient(clientUserId) { id, clientUserId_, weeklyLimit, createdAt ->
    Budget_goals(
      id,
      clientUserId_,
      weeklyLimit,
      createdAt
    )
  }

  public fun <T : Any> selectFeedbackByClient(clientUserId: Long, mapper: (
    id: Long,
    clientUserId: Long,
    targetBusinessId: Long,
    category: String,
    message: String,
    isAnonymous: Long,
    status: String,
    createdAt: Long,
  ) -> T): Query<T> = SelectFeedbackByClientQuery(clientUserId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectFeedbackByClient(clientUserId: Long): Query<Feedback_reports> =
      selectFeedbackByClient(clientUserId) { id, clientUserId_, targetBusinessId, category, message,
      isAnonymous, status, createdAt ->
    Feedback_reports(
      id,
      clientUserId_,
      targetBusinessId,
      category,
      message,
      isAnonymous,
      status,
      createdAt
    )
  }

  public fun <T : Any> selectAllFeedback(mapper: (
    id: Long,
    clientUserId: Long,
    targetBusinessId: Long,
    category: String,
    message: String,
    isAnonymous: Long,
    status: String,
    createdAt: Long,
  ) -> T): Query<T> = Query(422_661_960, arrayOf("feedback_reports"), driver, "Duka.sq",
      "selectAllFeedback",
      "SELECT feedback_reports.id, feedback_reports.clientUserId, feedback_reports.targetBusinessId, feedback_reports.category, feedback_reports.message, feedback_reports.isAnonymous, feedback_reports.status, feedback_reports.createdAt FROM feedback_reports ORDER BY createdAt DESC") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectAllFeedback(): Query<Feedback_reports> = selectAllFeedback { id, clientUserId,
      targetBusinessId, category, message, isAnonymous, status, createdAt ->
    Feedback_reports(
      id,
      clientUserId,
      targetBusinessId,
      category,
      message,
      isAnonymous,
      status,
      createdAt
    )
  }

  public fun <T : Any> selectShopRatingByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    averageRating: Double,
    ratingCount: Long,
  ) -> T): Query<T> = SelectShopRatingByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getDouble(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun selectShopRatingByBusiness(businessId: Long): Query<Shop_ratings> =
      selectShopRatingByBusiness(businessId) { id, businessId_, averageRating, ratingCount ->
    Shop_ratings(
      id,
      businessId_,
      averageRating,
      ratingCount
    )
  }

  public fun <T : Any> selectAllShopRatings(mapper: (
    id: Long,
    businessId: Long,
    averageRating: Double,
    ratingCount: Long,
  ) -> T): Query<T> = Query(2_038_005_757, arrayOf("shop_ratings"), driver, "Duka.sq",
      "selectAllShopRatings",
      "SELECT shop_ratings.id, shop_ratings.businessId, shop_ratings.averageRating, shop_ratings.ratingCount FROM shop_ratings ORDER BY averageRating DESC") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getDouble(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun selectAllShopRatings(): Query<Shop_ratings> = selectAllShopRatings { id, businessId,
      averageRating, ratingCount ->
    Shop_ratings(
      id,
      businessId,
      averageRating,
      ratingCount
    )
  }

  public fun <T : Any> selectAllWholesalers(mapper: (
    id: Long,
    name: String,
    specialty: String,
    rating: Double,
    deliveryEstimate: String,
  ) -> T): Query<T> = Query(944_420_508, arrayOf("wholesalers"), driver, "Duka.sq",
      "selectAllWholesalers",
      "SELECT wholesalers.id, wholesalers.name, wholesalers.specialty, wholesalers.rating, wholesalers.deliveryEstimate FROM wholesalers ORDER BY rating DESC") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getString(4)!!
    )
  }

  public fun selectAllWholesalers(): Query<Wholesalers> = selectAllWholesalers { id, name,
      specialty, rating, deliveryEstimate ->
    Wholesalers(
      id,
      name,
      specialty,
      rating,
      deliveryEstimate
    )
  }

  public fun <T : Any> selectWholesalersByQuery(
    `value`: String,
    value_: String,
    mapper: (
      id: Long,
      name: String,
      specialty: String,
      rating: Double,
      deliveryEstimate: String,
    ) -> T,
  ): Query<T> = SelectWholesalersByQueryQuery(value, value_) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getString(4)!!
    )
  }

  public fun selectWholesalersByQuery(value_: String, value__: String): Query<Wholesalers> =
      selectWholesalersByQuery(value_, value__) { id, name, specialty, rating, deliveryEstimate ->
    Wholesalers(
      id,
      name,
      specialty,
      rating,
      deliveryEstimate
    )
  }

  public fun <T : Any> selectWholesalerById(id: Long, mapper: (
    id: Long,
    name: String,
    specialty: String,
    rating: Double,
    deliveryEstimate: String,
  ) -> T): Query<T> = SelectWholesalerByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getDouble(3)!!,
      cursor.getString(4)!!
    )
  }

  public fun selectWholesalerById(id: Long): Query<Wholesalers> = selectWholesalerById(id) { id_,
      name, specialty, rating, deliveryEstimate ->
    Wholesalers(
      id_,
      name,
      specialty,
      rating,
      deliveryEstimate
    )
  }

  public fun <T : Any> selectRestockRequestsByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    productId: Long,
    wholesalerId: Long,
    quantity: Long,
    status: String,
    requestedAt: Long,
  ) -> T): Query<T> = SelectRestockRequestsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectRestockRequestsByBusiness(businessId: Long): Query<Restock_requests> =
      selectRestockRequestsByBusiness(businessId) { id, businessId_, productId, wholesalerId,
      quantity, status, requestedAt ->
    Restock_requests(
      id,
      businessId_,
      productId,
      wholesalerId,
      quantity,
      status,
      requestedAt
    )
  }

  public fun <T : Any> selectStockAdjustmentsByBusiness(businessId: Long, mapper: (
    id: Long,
    productId: Long,
    businessId: Long,
    delta: Long,
    reason: String,
    timestamp: Long,
  ) -> T): Query<T> = SelectStockAdjustmentsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectStockAdjustmentsByBusiness(businessId: Long): Query<Stock_adjustments> =
      selectStockAdjustmentsByBusiness(businessId) { id, productId, businessId_, delta, reason,
      timestamp ->
    Stock_adjustments(
      id,
      productId,
      businessId_,
      delta,
      reason,
      timestamp
    )
  }

  public fun <T : Any> selectStockAdjustmentsByProduct(productId: Long, mapper: (
    id: Long,
    productId: Long,
    businessId: Long,
    delta: Long,
    reason: String,
    timestamp: Long,
  ) -> T): Query<T> = SelectStockAdjustmentsByProductQuery(productId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!
    )
  }

  public fun selectStockAdjustmentsByProduct(productId: Long): Query<Stock_adjustments> =
      selectStockAdjustmentsByProduct(productId) { id, productId_, businessId, delta, reason,
      timestamp ->
    Stock_adjustments(
      id,
      productId_,
      businessId,
      delta,
      reason,
      timestamp
    )
  }

  public fun <T : Any> selectUnitsSoldInRange(
    productId: Long,
    timestamp: Long,
    timestamp_: Long,
    mapper: (SUM: Long?) -> T,
  ): Query<T> = SelectUnitsSoldInRangeQuery(productId, timestamp, timestamp_) { cursor ->
    mapper(
      cursor.getLong(0)
    )
  }

  public fun selectUnitsSoldInRange(
    productId: Long,
    timestamp: Long,
    timestamp_: Long,
  ): Query<SelectUnitsSoldInRange> = selectUnitsSoldInRange(productId, timestamp, timestamp_) {
      SUM ->
    SelectUnitsSoldInRange(
      SUM
    )
  }

  public fun <T : Any> selectAlertsByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    productId: Long,
    type: String,
    message: String,
    severity: String,
    isRead: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectAlertsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectAlertsByBusiness(businessId: Long): Query<Product_alerts> =
      selectAlertsByBusiness(businessId) { id, businessId_, productId, type, message, severity,
      isRead, createdAt ->
    Product_alerts(
      id,
      businessId_,
      productId,
      type,
      message,
      severity,
      isRead,
      createdAt
    )
  }

  public fun <T : Any> selectUnreadAlertsByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    productId: Long,
    type: String,
    message: String,
    severity: String,
    isRead: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectUnreadAlertsByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectUnreadAlertsByBusiness(businessId: Long): Query<Product_alerts> =
      selectUnreadAlertsByBusiness(businessId) { id, businessId_, productId, type, message,
      severity, isRead, createdAt ->
    Product_alerts(
      id,
      businessId_,
      productId,
      type,
      message,
      severity,
      isRead,
      createdAt
    )
  }

  public fun selectUnreadAlertCount(businessId: Long): Query<Long> =
      SelectUnreadAlertCountQuery(businessId) { cursor ->
    cursor.getLong(0)!!
  }

  public fun <T : Any> selectUnreadAlertByProduct(
    businessId: Long,
    productId: Long,
    mapper: (
      id: Long,
      businessId: Long,
      productId: Long,
      type: String,
      message: String,
      severity: String,
      isRead: Long,
      createdAt: Long,
    ) -> T,
  ): Query<T> = SelectUnreadAlertByProductQuery(businessId, productId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getLong(6)!!,
      cursor.getLong(7)!!
    )
  }

  public fun selectUnreadAlertByProduct(businessId: Long, productId: Long): Query<Product_alerts> =
      selectUnreadAlertByProduct(businessId, productId) { id, businessId_, productId_, type,
      message, severity, isRead, createdAt ->
    Product_alerts(
      id,
      businessId_,
      productId_,
      type,
      message,
      severity,
      isRead,
      createdAt
    )
  }

  public fun <T : Any> selectNotificationsByUser(userId: Long, mapper: (
    id: Long,
    userId: Long,
    businessId: Long,
    type: String,
    title: String,
    body: String,
    actionRoute: String,
    isRead: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectNotificationsByUserQuery(userId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!
    )
  }

  public fun selectNotificationsByUser(userId: Long): Query<App_notifications> =
      selectNotificationsByUser(userId) { id, userId_, businessId, type, title, body, actionRoute,
      isRead, createdAt ->
    App_notifications(
      id,
      userId_,
      businessId,
      type,
      title,
      body,
      actionRoute,
      isRead,
      createdAt
    )
  }

  public fun <T : Any> selectUnreadNotificationsByUser(userId: Long, mapper: (
    id: Long,
    userId: Long,
    businessId: Long,
    type: String,
    title: String,
    body: String,
    actionRoute: String,
    isRead: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectUnreadNotificationsByUserQuery(userId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!
    )
  }

  public fun selectUnreadNotificationsByUser(userId: Long): Query<App_notifications> =
      selectUnreadNotificationsByUser(userId) { id, userId_, businessId, type, title, body,
      actionRoute, isRead, createdAt ->
    App_notifications(
      id,
      userId_,
      businessId,
      type,
      title,
      body,
      actionRoute,
      isRead,
      createdAt
    )
  }

  public fun selectUnreadNotificationCount(userId: Long): Query<Long> =
      SelectUnreadNotificationCountQuery(userId) { cursor ->
    cursor.getLong(0)!!
  }

  public fun <T : Any> selectNotificationsByUserAndTypes(
    userId: Long,
    type: Collection<String>,
    mapper: (
      id: Long,
      userId: Long,
      businessId: Long,
      type: String,
      title: String,
      body: String,
      actionRoute: String,
      isRead: Long,
      createdAt: Long,
    ) -> T,
  ): Query<T> = SelectNotificationsByUserAndTypesQuery(userId, type) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getLong(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getString(5)!!,
      cursor.getString(6)!!,
      cursor.getLong(7)!!,
      cursor.getLong(8)!!
    )
  }

  public fun selectNotificationsByUserAndTypes(userId: Long, type: Collection<String>):
      Query<App_notifications> = selectNotificationsByUserAndTypes(userId, type) { id, userId_,
      businessId, type_, title, body, actionRoute, isRead, createdAt ->
    App_notifications(
      id,
      userId_,
      businessId,
      type_,
      title,
      body,
      actionRoute,
      isRead,
      createdAt
    )
  }

  public fun <T : Any> selectExpensesByBusiness(businessId: Long, mapper: (
    id: Long,
    businessId: Long,
    label: String,
    amount: Long,
    periodStart: Long,
    periodEnd: Long,
    createdAt: Long,
  ) -> T): Query<T> = SelectExpensesByBusinessQuery(businessId) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!,
      cursor.getLong(4)!!,
      cursor.getLong(5)!!,
      cursor.getLong(6)!!
    )
  }

  public fun selectExpensesByBusiness(businessId: Long): Query<Expenses> =
      selectExpensesByBusiness(businessId) { id, businessId_, label, amount, periodStart, periodEnd,
      createdAt ->
    Expenses(
      id,
      businessId_,
      label,
      amount,
      periodStart,
      periodEnd,
      createdAt
    )
  }

  public fun <T : Any> selectTotalExpensesInRange(
    businessId: Long,
    periodStart: Long,
    periodEnd: Long,
    mapper: (SUM: Long?) -> T,
  ): Query<T> = SelectTotalExpensesInRangeQuery(businessId, periodStart, periodEnd) { cursor ->
    mapper(
      cursor.getLong(0)
    )
  }

  public fun selectTotalExpensesInRange(
    businessId: Long,
    periodStart: Long,
    periodEnd: Long,
  ): Query<SelectTotalExpensesInRange> = selectTotalExpensesInRange(businessId, periodStart,
      periodEnd) { SUM ->
    SelectTotalExpensesInRange(
      SUM
    )
  }

  public fun insertBusiness(
    name: String,
    type: String,
    employeeCount: Long,
    language: String,
    district: String,
    createdAt: Long,
  ) {
    driver.execute(-2_048_280_389, """
        |INSERT INTO businesses(name, type, employeeCount, language, district, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindString(0, name)
          bindString(1, type)
          bindLong(2, employeeCount)
          bindString(3, language)
          bindString(4, district)
          bindLong(5, createdAt)
        }
    notifyQueries(-2_048_280_389) { emit ->
      emit("businesses")
    }
  }

  public fun insertUser(
    businessId: Long?,
    fullName: String,
    phoneOrEmail: String,
    passwordHash: String,
    role: String,
    isDeleted: Long,
    createdAt: Long,
  ) {
    driver.execute(-490_890_682, """
        |INSERT INTO users(businessId, fullName, phoneOrEmail, passwordHash, role, isDeleted, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindLong(0, businessId)
          bindString(1, fullName)
          bindString(2, phoneOrEmail)
          bindString(3, passwordHash)
          bindString(4, role)
          bindLong(5, isDeleted)
          bindLong(6, createdAt)
        }
    notifyQueries(-490_890_682) { emit ->
      emit("users")
    }
  }

  public fun updateUserName(fullName: String, id: Long) {
    driver.execute(-1_621_074_943, """UPDATE users SET fullName = ? WHERE id = ?""", 2) {
          bindString(0, fullName)
          bindLong(1, id)
        }
    notifyQueries(-1_621_074_943) { emit ->
      emit("users")
    }
  }

  public fun softDeleteUser(id: Long) {
    driver.execute(-985_540_862, """UPDATE users SET isDeleted = 1 WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(-985_540_862) { emit ->
      emit("users")
    }
  }

  public fun insertProduct(
    businessId: Long,
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    createdAt: Long,
    isDeleted: Long,
    costPrice: Long,
    lowStockThreshold: Long,
  ) {
    driver.execute(77_088_916, """
        |INSERT INTO products(businessId, name, price, stockQuantity, category, createdAt, isDeleted, costPrice, lowStockThreshold)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 9) {
          bindLong(0, businessId)
          bindString(1, name)
          bindDouble(2, price)
          bindLong(3, stockQuantity)
          bindString(4, category)
          bindLong(5, createdAt)
          bindLong(6, isDeleted)
          bindLong(7, costPrice)
          bindLong(8, lowStockThreshold)
        }
    notifyQueries(77_088_916) { emit ->
      emit("products")
    }
  }

  public fun updateProduct(
    name: String,
    price: Double,
    stockQuantity: Long,
    category: String,
    costPrice: Long,
    lowStockThreshold: Long,
    id: Long,
  ) {
    driver.execute(-907_353_980,
        """UPDATE products SET name = ?, price = ?, stockQuantity = ?, category = ?, costPrice = ?, lowStockThreshold = ? WHERE id = ?""",
        7) {
          bindString(0, name)
          bindDouble(1, price)
          bindLong(2, stockQuantity)
          bindString(3, category)
          bindLong(4, costPrice)
          bindLong(5, lowStockThreshold)
          bindLong(6, id)
        }
    notifyQueries(-907_353_980) { emit ->
      emit("products")
    }
  }

  public fun decrementStock(id: Long) {
    driver.execute(451_000_677,
        """UPDATE products SET stockQuantity = stockQuantity - 1 WHERE id = ? AND stockQuantity > 0""",
        1) {
          bindLong(0, id)
        }
    notifyQueries(451_000_677) { emit ->
      emit("products")
    }
  }

  public fun incrementStock(stockQuantity: Long, id: Long) {
    driver.execute(-422_452_215,
        """UPDATE products SET stockQuantity = stockQuantity + ? WHERE id = ?""", 2) {
          bindLong(0, stockQuantity)
          bindLong(1, id)
        }
    notifyQueries(-422_452_215) { emit ->
      emit("products")
    }
  }

  public fun softDeleteProduct(id: Long) {
    driver.execute(-13_630_888, """UPDATE products SET isDeleted = 1 WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(-13_630_888) { emit ->
      emit("products")
    }
  }

  public fun insertSale(
    productId: Long,
    businessId: Long,
    amount: Double,
    timestamp: Long,
    source: String,
  ) {
    driver.execute(-490_967_358, """
        |INSERT INTO sales(productId, businessId, amount, timestamp, source)
        |VALUES (?, ?, ?, ?, ?)
        """.trimMargin(), 5) {
          bindLong(0, productId)
          bindLong(1, businessId)
          bindDouble(2, amount)
          bindLong(3, timestamp)
          bindString(4, source)
        }
    notifyQueries(-490_967_358) { emit ->
      emit("sales")
    }
  }

  public fun updateEmployeeRole(role: String, id: Long) {
    driver.execute(1_494_463_951, """UPDATE employees SET role = ? WHERE id = ?""", 2) {
          bindString(0, role)
          bindLong(1, id)
        }
    notifyQueries(1_494_463_951) { emit ->
      emit("employees")
    }
  }

  public fun updateEmployeeActive(isActive: Long, id: Long) {
    driver.execute(1_163_248_159, """UPDATE employees SET isActive = ? WHERE id = ?""", 2) {
          bindLong(0, isActive)
          bindLong(1, id)
        }
    notifyQueries(1_163_248_159) { emit ->
      emit("employees")
    }
  }

  public fun updateEmployeeLastSeenAt(lastSeenAt: Long?, id: Long) {
    driver.execute(997_320_925, """UPDATE employees SET lastSeenAt = ? WHERE id = ?""", 2) {
          bindLong(0, lastSeenAt)
          bindLong(1, id)
        }
    notifyQueries(997_320_925) { emit ->
      emit("employees")
    }
  }

  public fun insertEmployee(
    businessId: Long,
    userId: Long,
    code: String,
    name: String,
    role: String,
    isActive: Long,
    joinedAt: Long,
    lastSeenAt: Long?,
  ) {
    driver.execute(292_020_137, """
        |INSERT INTO employees(businessId, userId, code, name, role, isActive, joinedAt, lastSeenAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 8) {
          bindLong(0, businessId)
          bindLong(1, userId)
          bindString(2, code)
          bindString(3, name)
          bindString(4, role)
          bindLong(5, isActive)
          bindLong(6, joinedAt)
          bindLong(7, lastSeenAt)
        }
    notifyQueries(292_020_137) { emit ->
      emit("employees")
    }
  }

  public fun deleteEmployee(id: Long) {
    driver.execute(1_210_880_155, """DELETE FROM employees WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(1_210_880_155) { emit ->
      emit("employees")
    }
  }

  public fun insertChatMessage(
    businessId: Long,
    senderRole: String,
    senderLabel: String,
    text: String,
    timestamp: Long,
  ) {
    driver.execute(-20_237_644, """
        |INSERT INTO chat_messages(businessId, senderRole, senderLabel, text, timestamp)
        |VALUES (?, ?, ?, ?, ?)
        """.trimMargin(), 5) {
          bindLong(0, businessId)
          bindString(1, senderRole)
          bindString(2, senderLabel)
          bindString(3, text)
          bindLong(4, timestamp)
        }
    notifyQueries(-20_237_644) { emit ->
      emit("chat_messages")
    }
  }

  public fun deleteClientMessages(businessId: Long) {
    driver.execute(965_681_764,
        """DELETE FROM chat_messages WHERE businessId = ? AND senderRole = 'client'""", 1) {
          bindLong(0, businessId)
        }
    notifyQueries(965_681_764) { emit ->
      emit("chat_messages")
    }
  }

  public fun insertPromo(
    businessId: Long,
    title: String,
    discountPercent: Long,
    startDate: Long,
    endDate: Long,
    isLive: Long,
  ) {
    driver.execute(1_957_620_212, """
        |INSERT INTO promos(businessId, title, discountPercent, startDate, endDate, isLive)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindLong(0, businessId)
          bindString(1, title)
          bindLong(2, discountPercent)
          bindLong(3, startDate)
          bindLong(4, endDate)
          bindLong(5, isLive)
        }
    notifyQueries(1_957_620_212) { emit ->
      emit("promos")
    }
  }

  public fun insertIssueReport(
    businessId: Long,
    category: String,
    message: String,
    attachBusinessId: Long,
    status: String,
    createdAt: Long,
  ) {
    driver.execute(-1_536_998_254, """
        |INSERT INTO issue_reports(businessId, category, message, attachBusinessId, status, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindLong(0, businessId)
          bindString(1, category)
          bindString(2, message)
          bindLong(3, attachBusinessId)
          bindString(4, status)
          bindLong(5, createdAt)
        }
    notifyQueries(-1_536_998_254) { emit ->
      emit("issue_reports")
    }
  }

  public fun insertEbmReceipt(
    saleId: Long,
    receiptNumber: String,
    status: String,
    sentAt: Long,
  ) {
    driver.execute(1_320_282_851, """
        |INSERT INTO ebm_receipts(saleId, receiptNumber, status, sentAt)
        |VALUES (?, ?, ?, ?)
        """.trimMargin(), 4) {
          bindLong(0, saleId)
          bindString(1, receiptNumber)
          bindString(2, status)
          bindLong(3, sentAt)
        }
    notifyQueries(1_320_282_851) { emit ->
      emit("ebm_receipts")
    }
  }

  public fun upsertTaxProfile(
    businessId: Long?,
    quarterlyTurnover: Double,
    tier: String,
    vatCollected: Double,
  ) {
    driver.execute(1_963_023_535, """
        |INSERT OR REPLACE INTO tax_profiles(businessId, quarterlyTurnover, tier, vatCollected)
        |VALUES (?, ?, ?, ?)
        """.trimMargin(), 4) {
          bindLong(0, businessId)
          bindDouble(1, quarterlyTurnover)
          bindString(2, tier)
          bindDouble(3, vatCollected)
        }
    notifyQueries(1_963_023_535) { emit ->
      emit("tax_profiles")
    }
  }

  public fun insertPurchase(
    clientUserId: Long,
    businessId: Long,
    productId: Long,
    amount: Double,
    timestamp: Long,
    receiptNumber: String,
  ) {
    driver.execute(841_874_940, """
        |INSERT INTO purchases(clientUserId, businessId, productId, amount, timestamp, receiptNumber)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindLong(0, clientUserId)
          bindLong(1, businessId)
          bindLong(2, productId)
          bindDouble(3, amount)
          bindLong(4, timestamp)
          bindString(5, receiptNumber)
        }
    notifyQueries(841_874_940) { emit ->
      emit("purchases")
    }
  }

  public fun upsertBudgetGoal(
    clientUserId: Long,
    weeklyLimit: Double,
    createdAt: Long,
  ) {
    driver.execute(1_783_716_137, """
        |INSERT OR REPLACE INTO budget_goals(clientUserId, weeklyLimit, createdAt)
        |VALUES (?, ?, ?)
        """.trimMargin(), 3) {
          bindLong(0, clientUserId)
          bindDouble(1, weeklyLimit)
          bindLong(2, createdAt)
        }
    notifyQueries(1_783_716_137) { emit ->
      emit("budget_goals")
    }
  }

  public fun insertFeedbackReport(
    clientUserId: Long,
    targetBusinessId: Long,
    category: String,
    message: String,
    isAnonymous: Long,
    status: String,
    createdAt: Long,
  ) {
    driver.execute(-990_984_748, """
        |INSERT INTO feedback_reports(clientUserId, targetBusinessId, category, message, isAnonymous, status, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindLong(0, clientUserId)
          bindLong(1, targetBusinessId)
          bindString(2, category)
          bindString(3, message)
          bindLong(4, isAnonymous)
          bindString(5, status)
          bindLong(6, createdAt)
        }
    notifyQueries(-990_984_748) { emit ->
      emit("feedback_reports")
    }
  }

  public fun upsertShopRating(
    businessId: Long,
    averageRating: Double,
    ratingCount: Long,
  ) {
    driver.execute(1_293_535_588, """
        |INSERT OR REPLACE INTO shop_ratings(businessId, averageRating, ratingCount)
        |VALUES (?, ?, ?)
        """.trimMargin(), 3) {
          bindLong(0, businessId)
          bindDouble(1, averageRating)
          bindLong(2, ratingCount)
        }
    notifyQueries(1_293_535_588) { emit ->
      emit("shop_ratings")
    }
  }

  public fun insertWholesaler(
    name: String,
    specialty: String,
    rating: Double,
    deliveryEstimate: String,
  ) {
    driver.execute(628_787_183, """
        |INSERT INTO wholesalers(name, specialty, rating, deliveryEstimate)
        |VALUES (?, ?, ?, ?)
        """.trimMargin(), 4) {
          bindString(0, name)
          bindString(1, specialty)
          bindDouble(2, rating)
          bindString(3, deliveryEstimate)
        }
    notifyQueries(628_787_183) { emit ->
      emit("wholesalers")
    }
  }

  public fun insertRestockRequest(
    businessId: Long,
    productId: Long,
    wholesalerId: Long,
    quantity: Long,
    status: String,
    requestedAt: Long,
  ) {
    driver.execute(-616_630_009, """
        |INSERT INTO restock_requests(businessId, productId, wholesalerId, quantity, status, requestedAt)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindLong(0, businessId)
          bindLong(1, productId)
          bindLong(2, wholesalerId)
          bindLong(3, quantity)
          bindString(4, status)
          bindLong(5, requestedAt)
        }
    notifyQueries(-616_630_009) { emit ->
      emit("restock_requests")
    }
  }

  public fun updateRestockRequestStatus(status: String, id: Long) {
    driver.execute(1_822_843_369, """UPDATE restock_requests SET status = ? WHERE id = ?""", 2) {
          bindString(0, status)
          bindLong(1, id)
        }
    notifyQueries(1_822_843_369) { emit ->
      emit("restock_requests")
    }
  }

  public fun insertStockAdjustment(
    productId: Long,
    businessId: Long,
    delta: Long,
    reason: String,
    timestamp: Long,
  ) {
    driver.execute(558_785_480, """
        |INSERT INTO stock_adjustments(productId, businessId, delta, reason, timestamp)
        |VALUES (?, ?, ?, ?, ?)
        """.trimMargin(), 5) {
          bindLong(0, productId)
          bindLong(1, businessId)
          bindLong(2, delta)
          bindString(3, reason)
          bindLong(4, timestamp)
        }
    notifyQueries(558_785_480) { emit ->
      emit("stock_adjustments")
    }
  }

  public fun insertProductAlert(
    businessId: Long,
    productId: Long,
    type: String,
    message: String,
    severity: String,
    isRead: Long,
    createdAt: Long,
  ) {
    driver.execute(-139_948_760, """
        |INSERT INTO product_alerts(businessId, productId, type, message, severity, isRead, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindLong(0, businessId)
          bindLong(1, productId)
          bindString(2, type)
          bindString(3, message)
          bindString(4, severity)
          bindLong(5, isRead)
          bindLong(6, createdAt)
        }
    notifyQueries(-139_948_760) { emit ->
      emit("product_alerts")
    }
  }

  public fun markAlertAsRead(id: Long) {
    driver.execute(1_118_210_421, """UPDATE product_alerts SET isRead = 1 WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(1_118_210_421) { emit ->
      emit("product_alerts")
    }
  }

  public fun markAllAlertsAsRead(businessId: Long) {
    driver.execute(-1_224_245_711, """UPDATE product_alerts SET isRead = 1 WHERE businessId = ?""",
        1) {
          bindLong(0, businessId)
        }
    notifyQueries(-1_224_245_711) { emit ->
      emit("product_alerts")
    }
  }

  public fun deleteAlert(id: Long) {
    driver.execute(924_905_103, """DELETE FROM product_alerts WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(924_905_103) { emit ->
      emit("product_alerts")
    }
  }

  public fun insertNotification(
    userId: Long,
    businessId: Long,
    type: String,
    title: String,
    body: String,
    actionRoute: String,
    isRead: Long,
    createdAt: Long,
  ) {
    driver.execute(-531_329_946, """
        |INSERT INTO app_notifications(userId, businessId, type, title, body, actionRoute, isRead, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 8) {
          bindLong(0, userId)
          bindLong(1, businessId)
          bindString(2, type)
          bindString(3, title)
          bindString(4, body)
          bindString(5, actionRoute)
          bindLong(6, isRead)
          bindLong(7, createdAt)
        }
    notifyQueries(-531_329_946) { emit ->
      emit("app_notifications")
    }
  }

  public fun markNotificationAsRead(id: Long) {
    driver.execute(1_339_567_234, """UPDATE app_notifications SET isRead = 1 WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(1_339_567_234) { emit ->
      emit("app_notifications")
    }
  }

  public fun markAllNotificationsAsRead(userId: Long) {
    driver.execute(1_167_722_462, """UPDATE app_notifications SET isRead = 1 WHERE userId = ?""", 1)
        {
          bindLong(0, userId)
        }
    notifyQueries(1_167_722_462) { emit ->
      emit("app_notifications")
    }
  }

  public fun deleteNotification(id: Long) {
    driver.execute(-762_088_360, """DELETE FROM app_notifications WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(-762_088_360) { emit ->
      emit("app_notifications")
    }
  }

  public fun insertExpense(
    businessId: Long,
    label: String,
    amount: Long,
    periodStart: Long,
    periodEnd: Long,
    createdAt: Long,
  ) {
    driver.execute(-922_795_011, """
        |INSERT INTO expenses(businessId, label, amount, periodStart, periodEnd, createdAt)
        |VALUES (?, ?, ?, ?, ?, ?)
        """.trimMargin(), 6) {
          bindLong(0, businessId)
          bindString(1, label)
          bindLong(2, amount)
          bindLong(3, periodStart)
          bindLong(4, periodEnd)
          bindLong(5, createdAt)
        }
    notifyQueries(-922_795_011) { emit ->
      emit("expenses")
    }
  }

  public fun deleteExpense(id: Long) {
    driver.execute(-616_059_701, """DELETE FROM expenses WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(-616_059_701) { emit ->
      emit("expenses")
    }
  }

  private inner class SelectBusinessByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("businesses", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("businesses", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(91_674_864,
        """SELECT businesses.id, businesses.name, businesses.type, businesses.employeeCount, businesses.language, businesses.district, businesses.createdAt FROM businesses WHERE id = ? LIMIT 1""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Duka.sq:selectBusinessById"
  }

  private inner class SelectUserByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("users", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("users", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_539_287_429,
        """SELECT users.id, users.businessId, users.fullName, users.phoneOrEmail, users.passwordHash, users.role, users.isDeleted, users.createdAt FROM users WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Duka.sq:selectUserById"
  }

  private inner class SelectUserByPhoneOrEmailQuery<out T : Any>(
    public val phoneOrEmail: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("users", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("users", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-230_189_173,
        """SELECT users.id, users.businessId, users.fullName, users.phoneOrEmail, users.passwordHash, users.role, users.isDeleted, users.createdAt FROM users WHERE phoneOrEmail = ? AND isDeleted = 0 LIMIT 1""",
        mapper, 1) {
      bindString(0, phoneOrEmail)
    }

    override fun toString(): String = "Duka.sq:selectUserByPhoneOrEmail"
  }

  private inner class SelectEmployeesByBusinessQuery<out T : Any>(
    public val businessId: Long?,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("users", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("users", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(null,
        """SELECT users.id, users.businessId, users.fullName, users.phoneOrEmail, users.passwordHash, users.role, users.isDeleted, users.createdAt FROM users WHERE businessId ${ if (businessId == null) "IS" else "=" } ? AND role = 'employee'""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectEmployeesByBusiness"
  }

  private inner class SelectEmployeeByNameQuery<out T : Any>(
    public val businessId: Long?,
    public val fullName: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("users", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("users", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(null,
        """SELECT users.id, users.businessId, users.fullName, users.phoneOrEmail, users.passwordHash, users.role, users.isDeleted, users.createdAt FROM users WHERE businessId ${ if (businessId == null) "IS" else "=" } ? AND role = 'employee' AND fullName = ? LIMIT 1""",
        mapper, 2) {
      bindLong(0, businessId)
      bindString(1, fullName)
    }

    override fun toString(): String = "Duka.sq:selectEmployeeByName"
  }

  private inner class SelectProductsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_018_237_831,
        """SELECT products.id, products.businessId, products.name, products.price, products.stockQuantity, products.category, products.createdAt, products.isDeleted, products.costPrice, products.lowStockThreshold FROM products WHERE businessId = ? AND isDeleted = 0 ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectProductsByBusiness"
  }

  private inner class SelectRecentProductsQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(2_100_085_405,
        """SELECT products.id, products.businessId, products.name, products.price, products.stockQuantity, products.category, products.createdAt, products.isDeleted, products.costPrice, products.lowStockThreshold FROM products WHERE businessId = ? AND isDeleted = 0 ORDER BY createdAt DESC LIMIT 5""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectRecentProducts"
  }

  private inner class SelectProductByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_051_835_293,
        """SELECT products.id, products.businessId, products.name, products.price, products.stockQuantity, products.category, products.createdAt, products.isDeleted, products.costPrice, products.lowStockThreshold FROM products WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Duka.sq:selectProductById"
  }

  private inner class SelectProductsByIdsQuery<out T : Any>(
    public val id: Collection<Long>,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> {
      val idIndexes = createArguments(count = id.size)
      return driver.executeQuery(null,
          """SELECT products.id, products.businessId, products.name, products.price, products.stockQuantity, products.category, products.createdAt, products.isDeleted, products.costPrice, products.lowStockThreshold FROM products WHERE id IN $idIndexes""",
          mapper, id.size) {
            id.forEachIndexed { index, id_ ->
              bindLong(index, id_)
            }
          }
    }

    override fun toString(): String = "Duka.sq:selectProductsByIds"
  }

  private inner class SelectCategoriesByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(891_560_881,
        """SELECT DISTINCT category FROM products WHERE businessId = ? AND isDeleted = 0 ORDER BY category""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectCategoriesByBusiness"
  }

  private inner class SelectAllActiveByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_038_656_928,
        """SELECT products.id, products.businessId, products.name, products.price, products.stockQuantity, products.category, products.createdAt, products.isDeleted, products.costPrice, products.lowStockThreshold FROM products WHERE businessId = ? AND isDeleted = 0""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectAllActiveByBusiness"
  }

  private inner class SelectSaleByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(663_260_407,
        """SELECT sales.id, sales.productId, sales.businessId, sales.amount, sales.timestamp, sales.source FROM sales WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Duka.sq:selectSaleById"
  }

  private inner class SelectSalesInRangeQuery<out T : Any>(
    public val businessId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(456_990_122,
        """SELECT sales.id, sales.productId, sales.businessId, sales.amount, sales.timestamp, sales.source FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?""",
        mapper, 3) {
      bindLong(0, businessId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectSalesInRange"
  }

  private inner class SelectTotalInRangeQuery<out T : Any>(
    public val businessId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_048_437_646,
        """SELECT SUM(amount) FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?""",
        mapper, 3) {
      bindLong(0, businessId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectTotalInRange"
  }

  private inner class SelectSalesCountInRangeQuery<out T : Any>(
    public val businessId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_508_062_281,
        """SELECT COUNT(*) FROM sales WHERE businessId = ? AND timestamp >= ? AND timestamp < ?""",
        mapper, 3) {
      bindLong(0, businessId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectSalesCountInRange"
  }

  private inner class SelectTotalAllTimeQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(407_828_840, """SELECT SUM(amount) FROM sales WHERE businessId = ?""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectTotalAllTime"
  }

  private inner class SelectLatestSaleQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_274_808_620,
        """SELECT sales.id, sales.productId, sales.businessId, sales.amount, sales.timestamp, sales.source FROM sales WHERE businessId = ? ORDER BY timestamp DESC LIMIT 1""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectLatestSale"
  }

  private inner class SelectAllByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_018_795_974,
        """SELECT sales.id, sales.productId, sales.businessId, sales.amount, sales.timestamp, sales.source FROM sales WHERE businessId = ?""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectAllByBusiness"
  }

  private inner class SelectAllTimeTopProductsQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(453_099_433,
        """SELECT productId, COUNT(*) AS cnt FROM sales WHERE businessId = ? GROUP BY productId ORDER BY cnt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectAllTimeTopProducts"
  }

  private inner class SelectRevenueByCategoryQuery<out T : Any>(
    public val businessId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", "sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", "sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(949_453_107, """
    |SELECT p.category, SUM(s.amount) AS total
    |FROM sales s INNER JOIN products p ON s.productId = p.id
    |WHERE s.businessId = ? AND s.timestamp BETWEEN ? AND ?
    |GROUP BY p.category
    """.trimMargin(), mapper, 3) {
      bindLong(0, businessId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectRevenueByCategory"
  }

  private inner class SelectRevenueBySourceQuery<out T : Any>(
    public val businessId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_937_946_000, """
    |SELECT source, SUM(amount) AS total
    |FROM sales WHERE businessId = ? AND timestamp BETWEEN ? AND ?
    |GROUP BY source
    """.trimMargin(), mapper, 3) {
      bindLong(0, businessId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectRevenueBySource"
  }

  private inner class SelectEmployeesByBusinessEmpQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("employees", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("employees", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_537_743_946,
        """SELECT employees.id, employees.businessId, employees.userId, employees.code, employees.name, employees.role, employees.isActive, employees.joinedAt, employees.lastSeenAt FROM employees WHERE businessId = ?""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectEmployeesByBusinessEmp"
  }

  private inner class SelectEmployeeByCodeQuery<out T : Any>(
    public val businessId: Long,
    public val code: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("employees", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("employees", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_391_891_696,
        """SELECT employees.id, employees.businessId, employees.userId, employees.code, employees.name, employees.role, employees.isActive, employees.joinedAt, employees.lastSeenAt FROM employees WHERE businessId = ? AND code = ? LIMIT 1""",
        mapper, 2) {
      bindLong(0, businessId)
      bindString(1, code)
    }

    override fun toString(): String = "Duka.sq:selectEmployeeByCode"
  }

  private inner class SelectActiveEmployeeByCodeQuery<out T : Any>(
    public val code: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("employees", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("employees", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-993_634_538,
        """SELECT employees.id, employees.businessId, employees.userId, employees.code, employees.name, employees.role, employees.isActive, employees.joinedAt, employees.lastSeenAt FROM employees WHERE code = ? AND isActive = 1 LIMIT 1""",
        mapper, 1) {
      bindString(0, code)
    }

    override fun toString(): String = "Duka.sq:selectActiveEmployeeByCode"
  }

  private inner class SelectEmployeeByCodeAnyQuery<out T : Any>(
    public val code: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("employees", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("employees", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_063_661_348,
        """SELECT employees.id, employees.businessId, employees.userId, employees.code, employees.name, employees.role, employees.isActive, employees.joinedAt, employees.lastSeenAt FROM employees WHERE code = ? LIMIT 1""",
        mapper, 1) {
      bindString(0, code)
    }

    override fun toString(): String = "Duka.sq:selectEmployeeByCodeAny"
  }

  private inner class SelectEmployeeByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("employees", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("employees", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-963_913_506,
        """SELECT employees.id, employees.businessId, employees.userId, employees.code, employees.name, employees.role, employees.isActive, employees.joinedAt, employees.lastSeenAt FROM employees WHERE id = ? LIMIT 1""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Duka.sq:selectEmployeeById"
  }

  private inner class SelectActiveEmployeeCountQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("employees", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("employees", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_354_094_237,
        """SELECT COUNT(*) FROM employees WHERE businessId = ? AND isActive = 1""", mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectActiveEmployeeCount"
  }

  private inner class SelectChatMessagesQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("chat_messages", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("chat_messages", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_264_142_850,
        """SELECT chat_messages.id, chat_messages.businessId, chat_messages.senderRole, chat_messages.senderLabel, chat_messages.text, chat_messages.timestamp FROM chat_messages WHERE businessId = ? ORDER BY timestamp ASC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectChatMessages"
  }

  private inner class SelectPromosByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("promos", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("promos", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(516_535_257,
        """SELECT promos.id, promos.businessId, promos.title, promos.discountPercent, promos.startDate, promos.endDate, promos.isLive FROM promos WHERE businessId = ? ORDER BY startDate DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectPromosByBusiness"
  }

  private inner class SelectLivePromosQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("promos", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("promos", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_112_293_362,
        """SELECT promos.id, promos.businessId, promos.title, promos.discountPercent, promos.startDate, promos.endDate, promos.isLive FROM promos WHERE businessId = ? AND isLive = 1""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectLivePromos"
  }

  private inner class SelectReportsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("issue_reports", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("issue_reports", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(2_140_873_176,
        """SELECT issue_reports.id, issue_reports.businessId, issue_reports.category, issue_reports.message, issue_reports.attachBusinessId, issue_reports.status, issue_reports.createdAt FROM issue_reports WHERE businessId = ? ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectReportsByBusiness"
  }

  private inner class SelectReceiptBySaleIdQuery<out T : Any>(
    public val saleId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("ebm_receipts", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("ebm_receipts", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_055_165_613,
        """SELECT ebm_receipts.id, ebm_receipts.saleId, ebm_receipts.receiptNumber, ebm_receipts.status, ebm_receipts.sentAt FROM ebm_receipts WHERE saleId = ?""",
        mapper, 1) {
      bindLong(0, saleId)
    }

    override fun toString(): String = "Duka.sq:selectReceiptBySaleId"
  }

  private inner class SelectReceiptsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("ebm_receipts", "sales", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("ebm_receipts", "sales", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_129_047_664, """
    |SELECT e.id, e.saleId, e.receiptNumber, e.status, e.sentAt FROM ebm_receipts e
    |JOIN sales s ON e.saleId = s.id
    |WHERE s.businessId = ?
    |ORDER BY e.sentAt DESC
    """.trimMargin(), mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectReceiptsByBusiness"
  }

  private inner class SelectTaxProfileQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("tax_profiles", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("tax_profiles", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(888_593_468,
        """SELECT tax_profiles.businessId, tax_profiles.quarterlyTurnover, tax_profiles.tier, tax_profiles.vatCollected FROM tax_profiles WHERE businessId = ?""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectTaxProfile"
  }

  private inner class SelectPurchasesByClientQuery<out T : Any>(
    public val clientUserId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("purchases", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("purchases", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_615_771_082,
        """SELECT purchases.id, purchases.clientUserId, purchases.businessId, purchases.productId, purchases.amount, purchases.timestamp, purchases.receiptNumber FROM purchases WHERE clientUserId = ? ORDER BY timestamp DESC""",
        mapper, 1) {
      bindLong(0, clientUserId)
    }

    override fun toString(): String = "Duka.sq:selectPurchasesByClient"
  }

  private inner class SelectTotalSpentInRangeQuery<out T : Any>(
    public val clientUserId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("purchases", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("purchases", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-638_074_704,
        """SELECT SUM(amount) FROM purchases WHERE clientUserId = ? AND timestamp >= ? AND timestamp < ?""",
        mapper, 3) {
      bindLong(0, clientUserId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectTotalSpentInRange"
  }

  private inner class SelectTotalAllTimeClientQuery<out T : Any>(
    public val clientUserId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("purchases", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("purchases", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(191_900_499,
        """SELECT SUM(amount) FROM purchases WHERE clientUserId = ?""", mapper, 1) {
      bindLong(0, clientUserId)
    }

    override fun toString(): String = "Duka.sq:selectTotalAllTimeClient"
  }

  private inner class SelectSpendingByCategoryQuery<out T : Any>(
    public val clientUserId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("products", "purchases", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("products", "purchases", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-664_546_793, """
    |SELECT p.category, SUM(pu.amount) AS total
    |FROM purchases pu INNER JOIN products p ON pu.productId = p.id
    |WHERE pu.clientUserId = ? AND pu.timestamp BETWEEN ? AND ?
    |GROUP BY p.category
    """.trimMargin(), mapper, 3) {
      bindLong(0, clientUserId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectSpendingByCategory"
  }

  private inner class SelectSpendingByBusinessQuery<out T : Any>(
    public val clientUserId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("purchases", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("purchases", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_861_888_807, """
    |SELECT businessId, SUM(amount) AS total
    |FROM purchases WHERE clientUserId = ? AND timestamp BETWEEN ? AND ?
    |GROUP BY businessId
    """.trimMargin(), mapper, 3) {
      bindLong(0, clientUserId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectSpendingByBusiness"
  }

  private inner class SelectBudgetGoalForClientQuery<out T : Any>(
    public val clientUserId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("budget_goals", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("budget_goals", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_148_823_006,
        """SELECT budget_goals.id, budget_goals.clientUserId, budget_goals.weeklyLimit, budget_goals.createdAt FROM budget_goals WHERE clientUserId = ? LIMIT 1""",
        mapper, 1) {
      bindLong(0, clientUserId)
    }

    override fun toString(): String = "Duka.sq:selectBudgetGoalForClient"
  }

  private inner class SelectFeedbackByClientQuery<out T : Any>(
    public val clientUserId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("feedback_reports", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("feedback_reports", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_859_820_571,
        """SELECT feedback_reports.id, feedback_reports.clientUserId, feedback_reports.targetBusinessId, feedback_reports.category, feedback_reports.message, feedback_reports.isAnonymous, feedback_reports.status, feedback_reports.createdAt FROM feedback_reports WHERE clientUserId = ? ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, clientUserId)
    }

    override fun toString(): String = "Duka.sq:selectFeedbackByClient"
  }

  private inner class SelectShopRatingByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("shop_ratings", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("shop_ratings", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_834_461_944,
        """SELECT shop_ratings.id, shop_ratings.businessId, shop_ratings.averageRating, shop_ratings.ratingCount FROM shop_ratings WHERE businessId = ?""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectShopRatingByBusiness"
  }

  private inner class SelectWholesalersByQueryQuery<out T : Any>(
    public val `value`: String,
    public val value_: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("wholesalers", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("wholesalers", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_430_410_448,
        """SELECT wholesalers.id, wholesalers.name, wholesalers.specialty, wholesalers.rating, wholesalers.deliveryEstimate FROM wholesalers WHERE name LIKE '%' || ? || '%' OR specialty LIKE '%' || ? || '%'""",
        mapper, 2) {
      bindString(0, value)
      bindString(1, value_)
    }

    override fun toString(): String = "Duka.sq:selectWholesalersByQuery"
  }

  private inner class SelectWholesalerByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("wholesalers", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("wholesalers", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_913_484_956,
        """SELECT wholesalers.id, wholesalers.name, wholesalers.specialty, wholesalers.rating, wholesalers.deliveryEstimate FROM wholesalers WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Duka.sq:selectWholesalerById"
  }

  private inner class SelectRestockRequestsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("restock_requests", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("restock_requests", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_364_233_664,
        """SELECT restock_requests.id, restock_requests.businessId, restock_requests.productId, restock_requests.wholesalerId, restock_requests.quantity, restock_requests.status, restock_requests.requestedAt FROM restock_requests WHERE businessId = ? ORDER BY requestedAt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectRestockRequestsByBusiness"
  }

  private inner class SelectStockAdjustmentsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("stock_adjustments", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("stock_adjustments", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(2_112_705_989,
        """SELECT stock_adjustments.id, stock_adjustments.productId, stock_adjustments.businessId, stock_adjustments.delta, stock_adjustments.reason, stock_adjustments.timestamp FROM stock_adjustments WHERE businessId = ? ORDER BY timestamp DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectStockAdjustmentsByBusiness"
  }

  private inner class SelectStockAdjustmentsByProductQuery<out T : Any>(
    public val productId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("stock_adjustments", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("stock_adjustments", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-897_064_374,
        """SELECT stock_adjustments.id, stock_adjustments.productId, stock_adjustments.businessId, stock_adjustments.delta, stock_adjustments.reason, stock_adjustments.timestamp FROM stock_adjustments WHERE productId = ? ORDER BY timestamp DESC""",
        mapper, 1) {
      bindLong(0, productId)
    }

    override fun toString(): String = "Duka.sq:selectStockAdjustmentsByProduct"
  }

  private inner class SelectUnitsSoldInRangeQuery<out T : Any>(
    public val productId: Long,
    public val timestamp: Long,
    public val timestamp_: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("stock_adjustments", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("stock_adjustments", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-272_959_757,
        """SELECT SUM(delta) FROM stock_adjustments WHERE productId = ? AND reason = 'sale' AND timestamp >= ? AND timestamp < ?""",
        mapper, 3) {
      bindLong(0, productId)
      bindLong(1, timestamp)
      bindLong(2, timestamp_)
    }

    override fun toString(): String = "Duka.sq:selectUnitsSoldInRange"
  }

  private inner class SelectAlertsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("product_alerts", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("product_alerts", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-931_745_428,
        """SELECT product_alerts.id, product_alerts.businessId, product_alerts.productId, product_alerts.type, product_alerts.message, product_alerts.severity, product_alerts.isRead, product_alerts.createdAt FROM product_alerts WHERE businessId = ? ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectAlertsByBusiness"
  }

  private inner class SelectUnreadAlertsByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("product_alerts", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("product_alerts", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_680_538_437,
        """SELECT product_alerts.id, product_alerts.businessId, product_alerts.productId, product_alerts.type, product_alerts.message, product_alerts.severity, product_alerts.isRead, product_alerts.createdAt FROM product_alerts WHERE businessId = ? AND isRead = 0 ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectUnreadAlertsByBusiness"
  }

  private inner class SelectUnreadAlertCountQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("product_alerts", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("product_alerts", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_149_922_400,
        """SELECT COUNT(*) FROM product_alerts WHERE businessId = ? AND isRead = 0""", mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectUnreadAlertCount"
  }

  private inner class SelectUnreadAlertByProductQuery<out T : Any>(
    public val businessId: Long,
    public val productId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("product_alerts", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("product_alerts", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_027_988_631,
        """SELECT product_alerts.id, product_alerts.businessId, product_alerts.productId, product_alerts.type, product_alerts.message, product_alerts.severity, product_alerts.isRead, product_alerts.createdAt FROM product_alerts WHERE businessId = ? AND productId = ? AND isRead = 0 ORDER BY createdAt DESC LIMIT 1""",
        mapper, 2) {
      bindLong(0, businessId)
      bindLong(1, productId)
    }

    override fun toString(): String = "Duka.sq:selectUnreadAlertByProduct"
  }

  private inner class SelectNotificationsByUserQuery<out T : Any>(
    public val userId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("app_notifications", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("app_notifications", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(634_335_436,
        """SELECT app_notifications.id, app_notifications.userId, app_notifications.businessId, app_notifications.type, app_notifications.title, app_notifications.body, app_notifications.actionRoute, app_notifications.isRead, app_notifications.createdAt FROM app_notifications WHERE userId = ? ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, userId)
    }

    override fun toString(): String = "Duka.sq:selectNotificationsByUser"
  }

  private inner class SelectUnreadNotificationsByUserQuery<out T : Any>(
    public val userId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("app_notifications", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("app_notifications", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_401_939_741,
        """SELECT app_notifications.id, app_notifications.userId, app_notifications.businessId, app_notifications.type, app_notifications.title, app_notifications.body, app_notifications.actionRoute, app_notifications.isRead, app_notifications.createdAt FROM app_notifications WHERE userId = ? AND isRead = 0 ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, userId)
    }

    override fun toString(): String = "Duka.sq:selectUnreadNotificationsByUser"
  }

  private inner class SelectUnreadNotificationCountQuery<out T : Any>(
    public val userId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("app_notifications", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("app_notifications", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(722_712_311,
        """SELECT COUNT(*) FROM app_notifications WHERE userId = ? AND isRead = 0""", mapper, 1) {
      bindLong(0, userId)
    }

    override fun toString(): String = "Duka.sq:selectUnreadNotificationCount"
  }

  private inner class SelectNotificationsByUserAndTypesQuery<out T : Any>(
    public val userId: Long,
    public val type: Collection<String>,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("app_notifications", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("app_notifications", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> {
      val typeIndexes = createArguments(count = type.size)
      return driver.executeQuery(null,
          """SELECT app_notifications.id, app_notifications.userId, app_notifications.businessId, app_notifications.type, app_notifications.title, app_notifications.body, app_notifications.actionRoute, app_notifications.isRead, app_notifications.createdAt FROM app_notifications WHERE userId = ? AND type IN $typeIndexes ORDER BY createdAt DESC""",
          mapper, 1 + type.size) {
            bindLong(0, userId)
            type.forEachIndexed { index, type_ ->
              bindString(index + 1, type_)
            }
          }
    }

    override fun toString(): String = "Duka.sq:selectNotificationsByUserAndTypes"
  }

  private inner class SelectExpensesByBusinessQuery<out T : Any>(
    public val businessId: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("expenses", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("expenses", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_205_213_840,
        """SELECT expenses.id, expenses.businessId, expenses.label, expenses.amount, expenses.periodStart, expenses.periodEnd, expenses.createdAt FROM expenses WHERE businessId = ? ORDER BY createdAt DESC""",
        mapper, 1) {
      bindLong(0, businessId)
    }

    override fun toString(): String = "Duka.sq:selectExpensesByBusiness"
  }

  private inner class SelectTotalExpensesInRangeQuery<out T : Any>(
    public val businessId: Long,
    public val periodStart: Long,
    public val periodEnd: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("expenses", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("expenses", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(321_049_303,
        """SELECT SUM(amount) FROM expenses WHERE businessId = ? AND periodStart >= ? AND periodEnd <= ?""",
        mapper, 3) {
      bindLong(0, businessId)
      bindLong(1, periodStart)
      bindLong(2, periodEnd)
    }

    override fun toString(): String = "Duka.sq:selectTotalExpensesInRange"
  }
}
