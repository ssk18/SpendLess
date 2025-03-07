package com.ssk.spendless.auth.data.repository

import com.ssk.spendless.auth.data.local.UserDao
import com.ssk.spendless.auth.data.mappers.toUserEntity
import com.ssk.spendless.auth.domain.IUserRepository
import com.ssk.spendless.auth.domain.UserDataValidator
import com.ssk.spendless.auth.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userDataValidator: UserDataValidator
): IUserRepository {
    override suspend fun insertUser(user: User): Long {
        return withContext(Dispatchers.IO) {
            userDao.upsertUser(user.toUserEntity())
        }
    }
}