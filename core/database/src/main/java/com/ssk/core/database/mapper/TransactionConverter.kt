package com.ssk.core.database.mapper

import androidx.room.TypeConverter
import com.ssk.core.domain.model.Expense
import com.ssk.core.domain.model.Income
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType

object TransactionConverter {
    private const val INCOME_TYPE = "Income"

    @TypeConverter
    fun fromRepeatType(value: RepeatType): String = value.name

    @TypeConverter
    fun toRepeatType(value: String): RepeatType = enumValueOf(value)

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return when (value) {
            is Income -> INCOME_TYPE
            is Expense -> value.name
        }
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return when (value) {
            INCOME_TYPE -> Income
            else -> enumValueOf<Expense>(value)
        }
    }
}