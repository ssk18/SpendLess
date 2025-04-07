package com.ssk.auth.presentation.screens.user_preference

import com.ssk.core.presentation.ui.states.ExpenseFormatState

data class UserPreferenceState(
    val expensesFormatState: ExpenseFormatState = ExpenseFormatState()
) {
    val isStartButtonEnabled: Boolean
        get() {
            val decimalSeparator = expensesFormatState.decimalSeparator.separator
            val thousandsSeparator = expensesFormatState.thousandsSeparator.separator
            return decimalSeparator != thousandsSeparator
        }
}
