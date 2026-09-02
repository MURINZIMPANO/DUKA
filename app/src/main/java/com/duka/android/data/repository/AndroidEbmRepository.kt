package com.duka.android.data.repository

import com.duka.app.data.local.dao.EbmReceiptDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.EbmRepository
import com.duka.shared.domain.EbmReceipt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidEbmRepository @Inject constructor(
    private val ebmReceiptDao: EbmReceiptDao
) : EbmRepository {

    override suspend fun saveReceipt(receipt: EbmReceipt): Long =
        ebmReceiptDao.insert(receipt.toRoom())

    override suspend fun getBySaleId(saleId: Long): EbmReceipt? =
        ebmReceiptDao.getBySaleId(saleId)?.toShared()

    override fun getReceiptsByBusiness(businessId: Long): Flow<List<EbmReceipt>> =
        ebmReceiptDao.getReceiptsByBusiness(businessId).map { list -> list.map { it.toShared() } }
}
