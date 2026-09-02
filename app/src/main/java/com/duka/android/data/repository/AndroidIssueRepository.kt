package com.duka.android.data.repository

import com.duka.app.data.local.dao.IssueReportDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.IssueRepository
import com.duka.shared.domain.IssueReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidIssueRepository @Inject constructor(
    private val issueReportDao: IssueReportDao
) : IssueRepository {

    override suspend fun submitReport(report: IssueReport): Long =
        issueReportDao.insert(report.toRoom())

    override fun getReportsByBusiness(businessId: Long): Flow<List<IssueReport>> =
        issueReportDao.getReportsByBusiness(businessId).map { list -> list.map { it.toShared() } }
}
