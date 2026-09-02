package com.duka.app.data.repository

import com.duka.app.data.local.dao.CategoryRevenue
import com.duka.app.data.local.dao.ProductCount
import com.duka.app.data.local.dao.SourceRevenue
import com.duka.app.data.local.dao.SaleDao
import com.duka.app.data.local.entity.Sale
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleRepository @Inject constructor(
    private val saleDao: SaleDao
) {
    suspend fun createSale(sale: Sale): Long = saleDao.insert(sale)
    suspend fun getSaleById(saleId: Long): Sale? = saleDao.getSaleById(saleId)
    fun getSalesInRange(businessId: Long, startTime: Long, endTime: Long): Flow<List<Sale>> =
        saleDao.getSalesInRange(businessId, startTime, endTime)
    fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Double?> =
        saleDao.getTotalInRange(businessId, startTime, endTime)
    suspend fun getTotalInRangeOnce(businessId: Long, startTime: Long, endTime: Long): Double? =
        saleDao.getTotalInRangeOnce(businessId, startTime, endTime)
    fun getSalesCountInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Int> =
        saleDao.getSalesCountInRange(businessId, startTime, endTime)
    fun getTopProducts(businessId: Long, startTime: Long, endTime: Long): Flow<List<ProductCount>> =
        saleDao.getTopProducts(businessId, startTime, endTime)
    suspend fun getTotalAllTime(businessId: Long): Double? = saleDao.getTotalAllTime(businessId)
    suspend fun getLatestSale(businessId: Long): Sale? = saleDao.getLatestSale(businessId)

    // V5: Analytics methods
    suspend fun getAllByBusiness(businessId: Long): List<Sale> = saleDao.getAllByBusiness(businessId)
    suspend fun getByProductInRange(productId: Long, startTime: Long, endTime: Long): List<Sale> =
        saleDao.getByProductInRange(productId, startTime, endTime)
    suspend fun getAllTimeTopProducts(businessId: Long): List<ProductCount> =
        saleDao.getAllTimeTopProducts(businessId)
    suspend fun getSalesInRangeOnce(businessId: Long, startTime: Long, endTime: Long): List<Sale> =
        saleDao.getSalesInRangeOnce(businessId, startTime, endTime)

    suspend fun revenueByCategory(businessId: Long, from: Long, to: Long): List<CategoryRevenue> =
        saleDao.revenueByCategory(businessId, from, to)

    suspend fun revenueBySource(businessId: Long, from: Long, to: Long): List<SourceRevenue> =
        saleDao.revenueBySource(businessId, from, to)
}
