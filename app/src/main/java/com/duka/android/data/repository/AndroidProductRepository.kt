package com.duka.android.data.repository

import com.duka.app.data.local.dao.ProductDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.ProductRepository
import com.duka.shared.domain.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidProductRepository @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override suspend fun addProduct(product: Product): Long =
        productDao.insert(product.toRoom())

    override fun getProductsByBusiness(businessId: Long): Flow<List<Product>> =
        productDao.getProductsByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override fun getRecentProducts(businessId: Long): Flow<List<Product>> =
        productDao.getRecentProducts(businessId).map { list -> list.map { it.toShared() } }

    override fun searchProducts(businessId: Long, query: String): Flow<List<Product>> =
        productDao.searchProducts(businessId, query).map { list -> list.map { it.toShared() } }

    override suspend fun decrementStock(productId: Long): Boolean =
        productDao.decrementStock(productId) > 0

    override suspend fun incrementStock(productId: Long, amount: Int) =
        productDao.incrementStock(productId, amount)

    override suspend fun getProductById(id: Long): Product? =
        productDao.getProductById(id)?.toShared()

    override suspend fun getProductsByIds(ids: List<Long>): List<Product> =
        productDao.getProductsByIds(ids).map { it.toShared() }

    override fun getCategoriesByBusiness(businessId: Long): Flow<List<String>> =
        productDao.getCategoriesByBusiness(businessId)

    override suspend fun updateProduct(product: Product) =
        productDao.update(product.toRoom())

    override suspend fun softDelete(productId: Long) =
        productDao.softDelete(productId)

    override suspend fun getAllActiveByBusiness(businessId: Long): List<Product> =
        productDao.getAllActiveByBusiness(businessId).map { it.toShared() }
}
