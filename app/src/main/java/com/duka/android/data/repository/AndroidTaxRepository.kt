package com.duka.android.data.repository

import com.duka.app.data.local.dao.TaxProfileDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.TaxRepository
import com.duka.shared.domain.TaxProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidTaxRepository @Inject constructor(
    private val taxProfileDao: TaxProfileDao
) : TaxRepository {

    override suspend fun upsert(taxProfile: TaxProfile) =
        taxProfileDao.upsert(taxProfile.toRoom())

    override fun getTaxProfile(businessId: Long): Flow<TaxProfile?> =
        taxProfileDao.getTaxProfile(businessId).map { it?.toShared() }
}
