package com.ssk.core.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector

@Composable
fun SpendLessExpensesFormat(
    modifier: Modifier = Modifier,
    selectedFormat: ExpensesFormatUi,
    onOptionSelected: (ExpensesFormatUi) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Expense Format"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = ExpensesFormatUi.entries,
            selectedOption = selectedFormat,
            onOptionSelected = {
                onOptionSelected(it as ExpensesFormatUi)
            }
        )
    }
}