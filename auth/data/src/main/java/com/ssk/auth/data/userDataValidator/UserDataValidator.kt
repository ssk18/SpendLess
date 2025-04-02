package com.ssk.auth.data.userDataValidator

import com.ssk.auth.domain.IUserDataValidator
import com.ssk.auth.domain.UsernameValidationError
import com.ssk.auth.domain.UsernameValidationResult

class UserDataValidator : IUserDataValidator {
    override fun validateUsername(username: String): UsernameValidationResult = when {
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

    companion object {
        const val MIN_USERNAME_LENGTH = 3
        const val MAX_USERNAME_LENGTH = 14
    }
}
