package com.duka.app.data.repository

import com.duka.app.data.local.dao.IssueReportDao
import com.duka.app.data.local.entity.IssueReport
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssueRepository @Inject constructor(
    private val issueReportDao: IssueReportDao
) {
    suspend fun submitReport(report: IssueReport): Long = issueReportDao.insert(report)
    fun getReportsByBusiness(businessId: Long): Flow<List<IssueReport>> =
        issueReportDao.getReportsByBusiness(businessId)
}
