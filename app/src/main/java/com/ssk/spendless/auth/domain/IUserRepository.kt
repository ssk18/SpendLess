package com.ssk.spendless.auth.domain

import com.ssk.spendless.auth.domain.model.User

interface IUserRepository {
    suspend fun insertUser(user: User): Long
}