package com.ssk.core.database.mapper

import com.ssk.core.database.entity.UserEntity
import com.ssk.core.domain.model.User

fun UserEntity.toUserInfo(): User {
    return User(
        userId = this.userId,
        username = this.username,
        pinCode = this.pinCode
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.userId ?: 0L,
        username = this.username,
        pinCode = this.pinCode
    )
}