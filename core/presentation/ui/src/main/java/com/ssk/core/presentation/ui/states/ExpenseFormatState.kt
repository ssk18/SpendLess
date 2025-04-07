package com.ssk.core.presentation.ui.states

import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.ui.components.DecimalSeparator
import com.ssk.core.presentation.ui.components.ExpensesFormat
import com.ssk.core.presentation.ui.components.ThousandsSeparator

data class ExpenseFormatState(
    val expenseFormat: ExpensesFormat = ExpensesFormat.MIUNS,
    val currency: Currency = Currency.INR,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandsSeparator: ThousandsSeparator = ThousandsSeparator.DOT
) {
    val formattedString: String
        get() {
            val example = "${currency.symbol}10${thousandsSeparator.separator}382${decimalSeparator.separator}45"
            return when (expenseFormat) {
                ExpensesFormat.MIUNS -> "-$example"
                ExpensesFormat.BRACKETS -> "($example)"
            }
        }
}
