package com.duka.android.data.repository

import com.duka.app.data.local.dao.EmployeeDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.EmployeeRepository
import com.duka.shared.domain.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidEmployeeRepository @Inject constructor(
    private val employeeDao: EmployeeDao
) : EmployeeRepository {

    override suspend fun addEmployee(employee: Employee): Long =
        employeeDao.insert(employee.toRoom())

    override fun getEmployeesByBusiness(businessId: Long): Flow<List<Employee>> =
        employeeDao.getEmployeesByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override suspend fun getEmployeesByBusinessOnce(businessId: Long): List<Employee> =
        employeeDao.getEmployeesByBusinessOnce(businessId).map { it.toShared() }

    override suspend fun getByCode(businessId: Long, code: String): Employee? =
        employeeDao.getByCode(businessId, code)?.toShared()

    override suspend fun getActiveByCode(code: String): Employee? =
        employeeDao.getActiveByCode(code)?.toShared()

    override suspend fun getByCodeAny(code: String): Employee? =
        employeeDao.getByCodeAny(code)?.toShared()

    override suspend fun getById(id: Long): Employee? =
        employeeDao.getById(id)?.toShared()

    override suspend fun updateRole(employeeId: Long, role: String) =
        employeeDao.updateRole(employeeId, role)

    override suspend fun updateActive(employeeId: Long, isActive: Boolean) =
        employeeDao.updateActive(employeeId, isActive)

    override suspend fun updateLastSeenAt(employeeId: Long, timestamp: Long) =
        employeeDao.updateLastSeenAt(employeeId, timestamp)

    override fun getActiveEmployeeCount(businessId: Long): Flow<Int> =
        employeeDao.getActiveEmployeeCount(businessId)

    override suspend fun deleteById(employeeId: Long) =
        employeeDao.deleteById(employeeId)
}
