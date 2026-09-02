package com.duka.android.data.repository

import com.duka.app.data.local.dao.BusinessDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.BusinessRepository
import com.duka.shared.domain.Business
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidBusinessRepository @Inject constructor(
    private val businessDao: BusinessDao
) : BusinessRepository {

    override suspend fun createBusiness(business: Business): Long =
        businessDao.insert(business.toRoom())

    override fun getActiveBusiness(): Flow<Business?> =
        businessDao.getActiveBusiness().map { it?.toShared() }

    override suspend fun getActiveBusinessOnce(): Business? =
        businessDao.getActiveBusinessOnce()?.toShared()

    override suspend fun getById(id: Long): Business? =
        businessDao.getById(id)?.toShared()
}
