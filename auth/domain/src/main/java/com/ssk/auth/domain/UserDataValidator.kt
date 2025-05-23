package com.ssk.auth.domain

interface IUserDataValidator {
    fun validateUsername(username: String): UsernameValidationResult
}

data class UsernameValidationResult(
    val isValid: Boolean,
    val error: UsernameValidationError? = null
)

enum class UsernameValidationError {
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    INVALID_CHARACTERS
}

fun Long.formatToTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}