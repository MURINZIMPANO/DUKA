package com.duka.app.data.repository

import com.duka.app.data.local.dao.BusinessDao
import com.duka.app.data.local.entity.Business
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessRepository @Inject constructor(
    private val businessDao: BusinessDao
) {
    suspend fun createBusiness(business: Business): Long = businessDao.insert(business)
    fun getActiveBusiness(): Flow<Business?> = businessDao.getActiveBusiness()
    suspend fun getActiveBusinessOnce(): Business? = businessDao.getActiveBusinessOnce()

    /** Phase 4: one-shot lookup by id (owner-side client-purchase import). */
    suspend fun getBusinessById(id: Long): Business? = businessDao.getById(id)
    suspend fun getById(id: Long): Business? = businessDao.getById(id)
}
