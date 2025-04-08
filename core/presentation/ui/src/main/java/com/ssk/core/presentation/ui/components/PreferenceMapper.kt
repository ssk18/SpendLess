package com.ssk.core.presentation.ui.components

import com.ssk.core.domain.model.DecimalSeparator
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.domain.model.ThousandsSeparator

fun ExpensesFormatUi.toDomain(): ExpensesFormat {
    return when (this) {
        ExpensesFormatUi.MIUNS -> ExpensesFormat.MINUS
        ExpensesFormatUi.BRACKETS -> ExpensesFormat.BRACKETS
    }
}

fun DecimalSeparatorUi.toDomain(): DecimalSeparator {
    return when (this) {
        DecimalSeparatorUi.DOT -> DecimalSeparator.POINT
        DecimalSeparatorUi.COMMA -> DecimalSeparator.COMMA
    }
}

fun ThousandsSeparatorUi.toDomain(): ThousandsSeparator {
    return when (this) {
        ThousandsSeparatorUi.DOT -> ThousandsSeparator.POINT
        ThousandsSeparatorUi.COMMA -> ThousandsSeparator.COMMA
        ThousandsSeparatorUi.SPACE -> ThousandsSeparator.SPACE
    }
}

fun ExpensesFormat.toUi(): ExpensesFormatUi {
    return when (this) {
        ExpensesFormat.MINUS -> ExpensesFormatUi.MIUNS
        ExpensesFormat.BRACKETS -> ExpensesFormatUi.BRACKETS
    }
}

fun DecimalSeparator.toUi(): DecimalSeparatorUi {
    return when (this) {
        DecimalSeparator.POINT -> DecimalSeparatorUi.DOT
        DecimalSeparator.COMMA -> DecimalSeparatorUi.COMMA
    }
}

fun ThousandsSeparator.toUi(): ThousandsSeparatorUi {
    return when (this) {
        ThousandsSeparator.POINT -> ThousandsSeparatorUi.DOT
        ThousandsSeparator.COMMA -> ThousandsSeparatorUi.COMMA
        ThousandsSeparator.SPACE -> ThousandsSeparatorUi.SPACE
    }
}
