package com.duka.android.data.repository

import com.duka.app.data.local.dao.PromoDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.PromoRepository
import com.duka.shared.domain.Promo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPromoRepository @Inject constructor(
    private val promoDao: PromoDao
) : PromoRepository {

    override suspend fun addPromo(promo: Promo): Long =
        promoDao.insert(promo.toRoom())

    override fun getPromosByBusiness(businessId: Long): Flow<List<Promo>> =
        promoDao.getPromosByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override fun getLivePromos(businessId: Long): Flow<List<Promo>> =
        promoDao.getLivePromos(businessId).map { list -> list.map { it.toShared() } }
}
