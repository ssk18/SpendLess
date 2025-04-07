package com.ssk.auth.presentation.screens.user_preference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.ui.components.DecimalSeparator
import com.ssk.core.presentation.ui.components.ExpensesFormat
import com.ssk.core.presentation.ui.components.ThousandsSeparator
import com.ssk.core.presentation.ui.states.ExpenseFormatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserPreferencesViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(UserPreferenceState())
    val state = _state.asStateFlow()

    private val username = savedStateHandle.get<String>("username") ?: ""

    fun onAction(action: UserPreferenceAction) {
        when(action) {
            is UserPreferenceAction.OnCurrencyUpdate -> updateCurrency(action.currency)
            is UserPreferenceAction.OnDecimalSeparatorUpdate -> updateDecimalSeparator(action.format)
            is UserPreferenceAction.OnExpenseFormatUpdate -> updateExpenseFormat(action.format)
            is UserPreferenceAction.OnThousandsSeparatorUpdate -> updateThousandsSeparator(action.format)
        }
    }

    private fun updateCurrency(currency: Currency) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                currency = currency
            )
        }
    }

    private fun updateDecimalSeparator(decimalSeparator: DecimalSeparator) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                decimalSeparator = decimalSeparator
            )
        }
    }

    private fun updateThousandsSeparator(thousandsSeparator: ThousandsSeparator) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                thousandsSeparator = thousandsSeparator
            )
        }
    }

    private fun updateExpenseFormat(expensesFormat: ExpensesFormat) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                expenseFormat = expensesFormat
            )
        }
    }

    private fun updateExpenseFormatState(update: (ExpenseFormatState) -> ExpenseFormatState) {
        _state.update { currentState ->
            currentState.copy(
                expensesFormatState = update(currentState.expensesFormatState)
            )
        }
    }

}