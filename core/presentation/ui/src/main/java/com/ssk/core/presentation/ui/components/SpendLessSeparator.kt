package com.ssk.core.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector

@Composable
fun DecimalSeparator(
    modifier: Modifier = Modifier,
    selectedDecimal: DecimalSeparatorUi,
    onOptionSelected: (DecimalSeparatorUi) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Decimal Separator"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = DecimalSeparatorUi.entries,
            selectedOption = selectedDecimal,
            onOptionSelected = {

                onOptionSelected(it as DecimalSeparatorUi)
            }
        )
    }
}

@Composable
fun ThousandsSeparator(
    modifier: Modifier = Modifier,
    selectedThousands: ThousandsSeparatorUi,
    onOptionSelected: (ThousandsSeparatorUi) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Thousands Separator"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = ThousandsSeparatorUi.entries,
            selectedOption = selectedThousands,
            onOptionSelected = {
                onOptionSelected(it as ThousandsSeparatorUi)
            }
        )
    }
}