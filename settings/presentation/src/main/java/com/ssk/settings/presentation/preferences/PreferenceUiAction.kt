package com.ssk.settings.presentation.preferences

import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi

sealed interface PreferenceUiAction {
    data object OnSaveClicked : PreferenceUiAction
    data class OnDecimalSeparatorClicked(val decimalSeparator: DecimalSeparatorUi) :
        PreferenceUiAction

    data class OnThousandsSeparatorClicked(val thousandsSeparator: ThousandsSeparatorUi) :
        PreferenceUiAction

    data class OnCurrencySelected(val currency: Currency) : PreferenceUiAction
    data class OnExpenseFormatSelected(val expenseFormat: ExpensesFormatUi) : PreferenceUiAction
}