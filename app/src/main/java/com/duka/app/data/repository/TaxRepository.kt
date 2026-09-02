package com.duka.app.data.repository

import com.duka.app.data.local.dao.TaxProfileDao
import com.duka.app.data.local.entity.TaxProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaxRepository @Inject constructor(
    private val taxProfileDao: TaxProfileDao
) {
    suspend fun upsert(taxProfile: TaxProfile) = taxProfileDao.upsert(taxProfile)
    fun getTaxProfile(businessId: Long): Flow<TaxProfile?> = taxProfileDao.getTaxProfile(businessId)
}
