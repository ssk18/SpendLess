package com.ssk.spendless.auth.data.mappers

import com.ssk.spendless.auth.data.local.UserEntity
import com.ssk.spendless.auth.domain.model.User

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        username = username,
        id = 0
    )
}

fun UserEntity.toDomainModel(): User {
    return User(
        username = this.username
    )
}