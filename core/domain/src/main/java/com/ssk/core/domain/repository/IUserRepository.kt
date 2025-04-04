package com.ssk.core.domain.repository

import com.ssk.core.domain.model.User
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    suspend fun registerUser(user: User): Result<Long, DataError>

    suspend fun verifyPin(userName: String, pin: String): Result<Boolean, DataError>

    suspend fun getUser(userName: String): Result<User, DataError>

    fun getAllUsers(): Flow<Result<List<User>, DataError>>
}