package com.ssk.core.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.ssk.core.database.dao.UserDao
import com.ssk.core.database.mapper.toUserEntity
import com.ssk.core.database.mapper.toUserInfo
import com.ssk.core.domain.model.User
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UserRepository(
    private val userDao: UserDao
) : IUserRepository {
    
    override suspend fun getUserById(userId: Long): Result<User, DataError> {
        return try {
            val userEntity = userDao.getUserById(userId)
            
            userEntity?.let { entity ->
                Result.Success(entity.toUserInfo())
            } ?: run {
                Result.Error(DataError.Local.USER_FETCH_ERROR)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun upsertUser(user: User): Result<Long, DataError> {
        return try {
            val userId = userDao.upsertUser(user.toUserEntity())
            when {
                userId > 0 -> Result.Success(userId)
                userId == -1L -> Result.Error(DataError.Local.INSERT_USER_ERROR)
                else -> Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
        } catch (e: SQLiteConstraintException) {
            Result.Error(DataError.Local.DUPLICATE_USER_ERROR)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun verifyPin(
        userName: String,
        pin: String
    ): Result<Boolean, DataError> {
        return try {
            val userResult = getUser(userName)

            when (userResult) {
                is Result.Success -> {
                    val user = userResult.data
                    val isPinValid = user.pinCode == pin
                    if (!isPinValid) {
                        return Result.Error(DataError.Local.INVALID_CREDENTIALS)
                    } else {
                        Result.Success(isPinValid)
                    }
                }
                is Result.Error -> {
                    Result.Error(userResult.error)
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun getUser(userName: String): Result<User, DataError> {
        return try {
            val userEntity = userDao.getUser(userName)

            userEntity?.let { userEntity ->
                Result.Success(userEntity.toUserInfo())
            } ?: run {
                Result.Error(DataError.Local.USER_FETCH_ERROR)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }
    
    override fun getUserAsFlow(userName: String): Flow<Result<User, DataError>> {
        return userDao.observeUserByUsername(userName)
            .map { userEntity ->
                userEntity?.let { 
                    Result.Success(it.toUserInfo()) 
                } ?: Result.Error(DataError.Local.USER_FETCH_ERROR)
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getAllUsers(): Flow<Result<List<User>, DataError>> {
        return userDao.getAllUsers()
            .map { users ->
                Result.Success(users.map { it.toUserInfo() }) as Result<List<User>, DataError>
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(
                Dispatchers.IO
            )
    }
}