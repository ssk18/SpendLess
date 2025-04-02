package com.ssk.spendless.auth.data.mappers

import com.ssk.core.database.entity.UserEntity
import com.ssk.core.domain.model.User

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = 0,
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