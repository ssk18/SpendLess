package com.ssk.core.domain.model

data class User(
    val userId: Long? = null,
    val username: String,
    val pinCode: String,
    val settings: UserSettings
)