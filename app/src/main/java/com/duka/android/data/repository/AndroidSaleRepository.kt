package com.duka.android.data.repository

import com.duka.app.data.local.dao.SaleDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.SaleRepository
import com.duka.shared.domain.CategoryRevenue
import com.duka.shared.domain.ProductCount
import com.duka.shared.domain.Sale
import com.duka.shared.domain.SourceRevenue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidSaleRepository @Inject constructor(
    private val saleDao: SaleDao
) : SaleRepository {

    override suspend fun createSale(sale: Sale): Long =
        saleDao.insert(sale.toRoom())

    override suspend fun getSaleById(saleId: Long): Sale? =
        saleDao.getSaleById(saleId)?.toShared()

    override fun getSalesInRange(businessId: Long, startTime: Long, endTime: Long): Flow<List<Sale>> =
        saleDao.getSalesInRange(businessId, startTime, endTime).map { list -> list.map { it.toShared() } }

    override fun getTotalInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Double?> =
        saleDao.getTotalInRange(businessId, startTime, endTime)

    override suspend fun getTotalInRangeOnce(businessId: Long, startTime: Long, endTime: Long): Double? =
        saleDao.getTotalInRangeOnce(businessId, startTime, endTime)

    override fun getSalesCountInRange(businessId: Long, startTime: Long, endTime: Long): Flow<Int> =
        saleDao.getSalesCountInRange(businessId, startTime, endTime)

    override fun getTopProducts(businessId: Long, startTime: Long, endTime: Long): Flow<List<ProductCount>> =
        saleDao.getTopProducts(businessId, startTime, endTime).map { list -> list.map { it.toShared() } }

    override suspend fun getTotalAllTime(businessId: Long): Double? =
        saleDao.getTotalAllTime(businessId)

    override suspend fun getLatestSale(businessId: Long): Sale? =
        saleDao.getLatestSale(businessId)?.toShared()

    override suspend fun getAllByBusiness(businessId: Long): List<Sale> =
        saleDao.getAllByBusiness(businessId).map { it.toShared() }

    override suspend fun getByProductInRange(productId: Long, startTime: Long, endTime: Long): List<Sale> =
        saleDao.getByProductInRange(productId, startTime, endTime).map { it.toShared() }

    override suspend fun getAllTimeTopProducts(businessId: Long): List<ProductCount> =
        saleDao.getAllTimeTopProducts(businessId).map { it.toShared() }

    override suspend fun getSalesInRangeOnce(businessId: Long, startTime: Long, endTime: Long): List<Sale> =
        saleDao.getSalesInRangeOnce(businessId, startTime, endTime).map { it.toShared() }

    override suspend fun revenueByCategory(businessId: Long, from: Long, to: Long): List<CategoryRevenue> =
        saleDao.revenueByCategory(businessId, from, to).map { it.toShared() }

    override suspend fun revenueBySource(businessId: Long, from: Long, to: Long): List<SourceRevenue> =
        saleDao.revenueBySource(businessId, from, to).map { it.toShared() }
}
