package com.ssk.settings.presentation.preferences

import com.ssk.core.presentation.ui.states.ExpenseFormatState

data class PreferenceUiState(
    val expenseFormatState: ExpenseFormatState = ExpenseFormatState(),
) {
    val isSaveButtonEnabled: Boolean
        get() {
            val decimalSeparator = expenseFormatState.decimalSeparatorUi.separator
            val thousandsSeparator = expenseFormatState.thousandsSeparatorUi.separator
            return decimalSeparator != thousandsSeparator
        }
}