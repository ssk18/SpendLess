package com.ssk.core.database.mapper

import com.ssk.core.database.entity.UserEntity
import com.ssk.core.database.security.Crypto
import com.ssk.core.domain.model.BiometricsPrompt
import com.ssk.core.domain.model.Currency
import com.ssk.core.domain.model.DecimalSeparator
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.domain.model.SessionExpiryDuration
import com.ssk.core.domain.model.ThousandsSeparator
import com.ssk.core.domain.model.User
import com.ssk.core.domain.model.UserSettings

fun UserEntity.toUserInfo(): User {
    return User(
        userId = this.userId,
        username = this.username,
        pinCode = this.pinCode.decryptPin(),
        settings = UserSettings(
            expensesFormat = ExpensesFormat.valueOf(this.expensesFormat),
            currency = Currency.valueOf(this.currency),
            decimalSeparator = DecimalSeparator.valueOf(this.decimalSeparator),
            thousandsSeparator = ThousandsSeparator.valueOf(this.thousandsSeparator),
            biometricsPrompt = BiometricsPrompt.valueOf(this.biometricsPrompt),
            sessionExpiryDuration = SessionExpiryDuration.valueOf(this.sessionExpiryDuration),
            lockedOutDuration = LockedOutDuration.valueOf(this.lockedOutDuration)
        )
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.userId ?: 0L,
        username = this.username,
        pinCode = this.pinCode.encryptPin(),
        expensesFormat = this.settings.expensesFormat.name,
        currency = this.settings.currency.name,
        decimalSeparator = this.settings.decimalSeparator.name,
        thousandsSeparator = this.settings.thousandsSeparator.name,
        biometricsPrompt = this.settings.biometricsPrompt.name,
        sessionExpiryDuration = this.settings.sessionExpiryDuration.name,
        lockedOutDuration = this.settings.lockedOutDuration.name
    )
}

fun String.encryptPin(): ByteArray {
    return Crypto.encrypt(this.toByteArray())
}

fun ByteArray.decryptPin(): String {
    try {
        val decrypted = Crypto.decrypt(this)
        val result = decrypted.decodeToString()
        android.util.Log.d("UserInfoMapper", "Decrypted PIN successfully")
        return result
    } catch (e: Exception) {
        android.util.Log.e("UserInfoMapper", "Error decrypting PIN: ${e.message}", e)
        throw e
    }
}