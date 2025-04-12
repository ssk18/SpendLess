package com.ssk.core.database.mapper

import androidx.room.TypeConverter
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType

object TransactionConverter {
    @TypeConverter
    fun fromRepeatType(value: RepeatType): String = value.name

    @TypeConverter
    fun toRepeatType(value: String): RepeatType = enumValueOf(value)

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = enumValueOf(value)
}