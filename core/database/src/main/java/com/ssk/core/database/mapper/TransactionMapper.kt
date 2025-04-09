package com.ssk.core.database.mapper

import android.util.Base64
import com.ssk.core.database.entity.TransactionEntity
import com.ssk.core.database.security.Crypto
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.Transaction

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        userId = userId,
        title = Crypto.encrypt(title.toByteArray()).toBase64(),
        amount = Crypto.encrypt(amount.toString().toByteArray()).toBase64(),
        repeatType = Crypto.encrypt(repeatType.name.toByteArray()).toBase64(),
        transactionType = Crypto.encrypt(transactionType.toString().toByteArray()).toBase64(),
        note = note?.let { Crypto.encrypt(it.toByteArray()).toBase64() },
        transactionDate = Crypto.encrypt(transactionDate.toString().toByteArray()).toBase64()
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        userId = userId,
        title = Crypto.decrypt(title.fromBase64()).toString(Charsets.UTF_8),
        amount = Crypto.decrypt(amount.fromBase64()).toString(Charsets.UTF_8).toFloat(),
        repeatType = RepeatType.valueOf(Crypto.decrypt(repeatType.fromBase64()).toString(Charsets.UTF_8)),
        transactionType = TransactionConverter.toTransactionType(
            Crypto.decrypt(transactionType.fromBase64()).toString(Charsets.UTF_8)
        ),
        note = note?.let { Crypto.decrypt(it.fromBase64()).toString(Charsets.UTF_8) },
        transactionDate = Crypto.decrypt(transactionDate.fromBase64()).toString(Charsets.UTF_8).toLong()
    )
}

fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun String.fromBase64(): ByteArray = Base64.decode(this, Base64.DEFAULT)