package com.ssk.spendless.auth.data.mappers

import com.ssk.spendless.auth.data.local.UserEntity
import com.ssk.spendless.auth.domain.model.User

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = 0,
        username = username,
        pinCode = pinCode.toString()
    )
}

fun UserEntity.toDomainModel(): User {
    return User(
        username = this.username,
        pinCode = this.pinCode.toInt(),
    )
}