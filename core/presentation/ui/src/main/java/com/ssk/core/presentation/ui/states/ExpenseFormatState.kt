package com.ssk.core.presentation.ui.states

import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi

data class ExpenseFormatState(
    val expenseFormat: ExpensesFormatUi = ExpensesFormatUi.MINUS,
    val currency: Currency = Currency.INR,
    val decimalSeparatorUi: DecimalSeparatorUi = DecimalSeparatorUi.POINT,
    val thousandsSeparatorUi: ThousandsSeparatorUi = ThousandsSeparatorUi.DOT
) {
    val formattedString: String
        get() {
            val example = "${currency.symbol}10${thousandsSeparatorUi.separator}382${decimalSeparatorUi.separator}45"
            return when (expenseFormat) {
                ExpensesFormatUi.MINUS -> "-$example"
                ExpensesFormatUi.BRACKETS -> "($example)"
            }
        }
}
