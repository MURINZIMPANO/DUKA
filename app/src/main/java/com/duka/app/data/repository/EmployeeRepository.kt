package com.duka.app.data.repository

import com.duka.app.data.local.dao.EmployeeDao
import com.duka.app.data.local.entity.Employee
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepository @Inject constructor(
    private val employeeDao: EmployeeDao
) {
    suspend fun addEmployee(employee: Employee): Long = employeeDao.insert(employee)
    fun getEmployeesByBusiness(businessId: Long): Flow<List<Employee>> =
        employeeDao.getEmployeesByBusiness(businessId)
    suspend fun getEmployeesByBusinessOnce(businessId: Long): List<Employee> =
        employeeDao.getEmployeesByBusinessOnce(businessId)
    suspend fun getByCode(businessId: Long, code: String): Employee? =
        employeeDao.getByCode(businessId, code)
    suspend fun getActiveByCode(code: String): Employee? =
        employeeDao.getActiveByCode(code)
    suspend fun getByCodeAny(code: String): Employee? =
        employeeDao.getByCodeAny(code)
    suspend fun getById(id: Long): Employee? =
        employeeDao.getById(id)
    suspend fun updateRole(employeeId: Long, role: String) =
        employeeDao.updateRole(employeeId, role)
    suspend fun updateActive(employeeId: Long, isActive: Boolean) =
        employeeDao.updateActive(employeeId, isActive)
    suspend fun updateLastSeenAt(employeeId: Long, timestamp: Long) =
        employeeDao.updateLastSeenAt(employeeId, timestamp)
    fun getActiveEmployeeCount(businessId: Long): Flow<Int> =
        employeeDao.getActiveEmployeeCount(businessId)
    suspend fun deleteById(employeeId: Long) =
        employeeDao.deleteById(employeeId)
}
