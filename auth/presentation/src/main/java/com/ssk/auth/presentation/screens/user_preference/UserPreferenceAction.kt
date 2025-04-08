package com.ssk.auth.presentation.screens.user_preference

import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi

sealed interface UserPreferenceAction {
    data class OnExpenseFormatUpdate(val format: ExpensesFormatUi) : UserPreferenceAction
    data class OnCurrencyUpdate(val currency: Currency) : UserPreferenceAction
    data class OnDecimalSeparatorUpdate(val format: DecimalSeparatorUi) : UserPreferenceAction
    data class OnThousandsSeparatorUpdate(val format: ThousandsSeparatorUi) : UserPreferenceAction
    data object OnStartClicked: UserPreferenceAction
    data object OnBackClicked : UserPreferenceAction
}