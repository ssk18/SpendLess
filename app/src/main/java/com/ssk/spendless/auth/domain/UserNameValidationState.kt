package com.ssk.spendless.auth.domain

data class UserNameValidationState(
    val hasMinLength: Boolean = false,
    val hasMaxLength: Boolean = false,
    val hasNoSpace: Boolean = false,
    val isAlphaNumeric: Boolean = false
) {
    val isValidUsername: Boolean
        get() = hasMinLength && hasMaxLength && hasNoSpace || isAlphaNumeric
}