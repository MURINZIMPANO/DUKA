package com.duka.app.data.repository

import com.duka.app.data.local.dao.PromoDao
import com.duka.app.data.local.entity.Promo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromoRepository @Inject constructor(
    private val promoDao: PromoDao
) {
    suspend fun createPromo(promo: Promo): Long = promoDao.insert(promo)
    fun getPromosByBusiness(businessId: Long): Flow<List<Promo>> =
        promoDao.getPromosByBusiness(businessId)
    fun getLivePromos(businessId: Long): Flow<List<Promo>> =
        promoDao.getLivePromos(businessId)
}
