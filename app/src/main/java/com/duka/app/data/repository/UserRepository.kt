package com.duka.app.data.repository

import com.duka.app.data.local.dao.UserDao
import com.duka.app.data.local.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun createUser(user: User): Long = userDao.insert(user)
    suspend fun getById(id: Long): User? = userDao.getById(id)
    suspend fun getByPhoneOrEmail(phoneOrEmail: String): User? =
        userDao.getByPhoneOrEmail(normalizePhoneOrEmail(phoneOrEmail))
    fun getEmployeesByBusiness(businessId: Long): Flow<List<User>> =
        userDao.getEmployeesByBusiness(businessId)
    suspend fun getEmployeesByBusinessOnce(businessId: Long): List<User> =
        userDao.getEmployeesByBusinessOnce(businessId)
    suspend fun getEmployeeByName(businessId: Long, name: String): User? =
        userDao.getEmployeeByName(businessId, name)

    suspend fun updateName(userId: Long, name: String) = userDao.updateName(userId, name)

    suspend fun softDelete(userId: Long) = userDao.softDelete(userId)

    companion object {
        /**
         * Shared normalization for phoneOrEmail — used at both signup and login.
         * Trim + lowercase ensures "John@Email.com" and "john@email.com" match.
         * BUG FIX #3: This was the root cause of "account doesn't exist" after signup.
         */
        fun normalizePhoneOrEmail(raw: String): String = raw.trim().lowercase()
    }
}
