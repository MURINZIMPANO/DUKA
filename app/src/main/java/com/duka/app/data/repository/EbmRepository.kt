package com.duka.app.data.repository

import com.duka.app.data.local.dao.EbmReceiptDao
import com.duka.app.data.local.entity.EbmReceipt
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EbmRepository @Inject constructor(
    private val ebmReceiptDao: EbmReceiptDao
) {
    suspend fun saveReceipt(receipt: EbmReceipt): Long = ebmReceiptDao.insert(receipt)
    suspend fun getBySaleId(saleId: Long): EbmReceipt? = ebmReceiptDao.getBySaleId(saleId)
    fun getReceiptsByBusiness(businessId: Long): Flow<List<EbmReceipt>> =
        ebmReceiptDao.getReceiptsByBusiness(businessId)
}
