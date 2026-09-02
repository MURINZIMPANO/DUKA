package com.duka.android.data.repository

import com.duka.app.data.local.dao.UserDao
import com.duka.android.data.mappers.toRoom
import com.duka.android.data.mappers.toShared
import com.duka.shared.data.repository.UserRepository
import com.duka.shared.data.repository.normalizePhoneOrEmail
import com.duka.shared.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidUserRepository @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun createUser(user: User): Long =
        userDao.insert(user.toRoom())

    override suspend fun getById(id: Long): User? =
        userDao.getById(id)?.toShared()

    override suspend fun getByPhoneOrEmail(phoneOrEmail: String): User? =
        userDao.getByPhoneOrEmail(normalizePhoneOrEmail(phoneOrEmail))?.toShared()

    override fun getEmployeesByBusiness(businessId: Long): Flow<List<User>> =
        userDao.getEmployeesByBusiness(businessId).map { list -> list.map { it.toShared() } }

    override suspend fun getEmployeesByBusinessOnce(businessId: Long): List<User> =
        userDao.getEmployeesByBusinessOnce(businessId).map { it.toShared() }

    override suspend fun getEmployeeByName(businessId: Long, name: String): User? =
        userDao.getEmployeeByName(businessId, name)?.toShared()

    override suspend fun updateName(userId: Long, name: String) =
        userDao.updateName(userId, name)

    override suspend fun softDelete(userId: Long) =
        userDao.softDelete(userId)
}
