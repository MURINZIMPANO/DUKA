package com.duka.app.data.repository

import com.duka.app.data.local.dao.ProductDao
import com.duka.app.data.local.entity.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    suspend fun addProduct(product: Product): Long = productDao.insert(product)

    fun getProductsByBusiness(businessId: Long): Flow<List<Product>> =
        productDao.getProductsByBusiness(businessId)

    fun getRecentProducts(businessId: Long): Flow<List<Product>> =
        productDao.getRecentProducts(businessId)

    fun searchProducts(businessId: Long, query: String): Flow<List<Product>> =
        productDao.searchProducts(businessId, query)

    suspend fun decrementStock(productId: Long): Boolean = productDao.decrementStock(productId) > 0

    suspend fun incrementStock(productId: Long, amount: Int) = productDao.incrementStock(productId, amount)

    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    suspend fun getProductsByIds(ids: List<Long>): List<Product> = productDao.getProductsByIds(ids)

    fun getCategoriesByBusiness(businessId: Long): Flow<List<String>> =
        productDao.getCategoriesByBusiness(businessId)

    // V5: Product edit and delete
    suspend fun updateProduct(product: Product) = productDao.update(product)

    suspend fun softDelete(productId: Long) = productDao.softDelete(productId)

    suspend fun getAllActiveByBusiness(businessId: Long): List<Product> =
        productDao.getAllActiveByBusiness(businessId)
}
