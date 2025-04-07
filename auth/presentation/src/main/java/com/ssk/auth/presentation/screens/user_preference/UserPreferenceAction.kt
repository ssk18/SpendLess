package com.ssk.auth.presentation.screens.user_preference

import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.ui.components.DecimalSeparator
import com.ssk.core.presentation.ui.components.ExpensesFormat
import com.ssk.core.presentation.ui.components.ThousandsSeparator

sealed interface UserPreferenceAction {
    data class OnExpenseFormatUpdate(val format: ExpensesFormat) : UserPreferenceAction
    data class OnCurrencyUpdate(val currency: Currency) : UserPreferenceAction
    data class OnDecimalSeparatorUpdate(val format: DecimalSeparator) : UserPreferenceAction
    data class OnThousandsSeparatorUpdate(val format: ThousandsSeparator) : UserPreferenceAction
}