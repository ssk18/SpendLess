package com.ssk.spendless.auth.domain

class UserDataValidator {
    fun validateUsername(username: String): UsernameValidationResult = when {
        username.isBlank() -> UsernameValidationResult(
            isValid = false,
            error = UsernameValidationError.EMPTY
        )

        username.length < MIN_USERNAME_LENGTH -> UsernameValidationResult(
            isValid = false,
            error = UsernameValidationError.TOO_SHORT
        )

        username.length > MAX_USERNAME_LENGTH -> UsernameValidationResult(
            isValid = false,
            error = UsernameValidationError.TOO_LONG
        )

        !username.all { it.isLetterOrDigit() || it == '_' } -> UsernameValidationResult(
            isValid = false,
            error = UsernameValidationError.INVALID_CHARACTERS
        )

        else -> UsernameValidationResult(isValid = true)
    }

    fun isValidUsername(username: String): Boolean {
        return validateUsername(username).isValid
    }
    companion object {
        const val MIN_USERNAME_LENGTH = 3
        const val MAX_USERNAME_LENGTH = 14
    }
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