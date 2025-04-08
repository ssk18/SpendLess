package com.ssk.auth.presentation.screens.user_preference

import com.ssk.core.presentation.ui.states.ExpenseFormatState

data class UserPreferenceState(
    val expensesFormatState: ExpenseFormatState = ExpenseFormatState()
) {
    val isStartButtonEnabled: Boolean
        get() {
            val decimalSeparator = expensesFormatState.decimalSeparatorUi.separator
            val thousandsSeparator = expensesFormatState.thousandsSeparatorUi.separator
            return decimalSeparator != thousandsSeparator
        }
}
