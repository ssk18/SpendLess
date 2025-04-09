package com.ssk.core.domain.model

data class UserSettings(
    val expensesFormat: ExpensesFormat = ExpensesFormat.MINUS,
    val currency: Currency = Currency.USD,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.POINT,
    val thousandsSeparator: ThousandsSeparator = ThousandsSeparator.COMMA,
    val biometricsPrompt: BiometricsPrompt = BiometricsPrompt.DISABLE,
    val sessionExpiryDuration: SessionExpiryDuration = SessionExpiryDuration.FIVE_MIN,
    val lockedOutDuration: LockedOutDuration = LockedOutDuration.THIRTY_SEC
)
enum class ExpensesFormat {
    MINUS,
    BRACKETS,
}

enum class DecimalSeparator {
    POINT,
    COMMA
}

enum class ThousandsSeparator {
    POINT,
    COMMA,
    SPACE
}

enum class BiometricsPrompt {
    ENABLE,
    DISABLE
}

enum class SessionExpiryDuration {
    FIVE_MIN,
    FIFTEEN_MIN,
    THIRTY_MIN,
    HOUR
}

enum class LockedOutDuration {
    FIFTEEN_SEC,
    THIRTY_SEC,
    ONE_MIN,
    FIVE_MIN
}