package com.ssk.dashboard.presentation.dashboard.create_transaction.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssk.core.presentation.designsystem.components.SegmentOption
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.TrendingDownIcon
import com.ssk.core.presentation.designsystem.theme.TrendingUpIcon
import com.ssk.core.presentation.ui.components.OptionText

@Composable
fun TransactionTypeSelector(
    modifier: Modifier = Modifier,
    selectedOption: TransactionTypeOptions,
    onOptionSelected: (TransactionTypeOptions) -> Unit
) {
    SpendLessSegmentSelector(
        segmentOptions = TransactionTypeOptions.entries,
        selectedOption = selectedOption,
        onOptionSelected = { onOptionSelected(it as TransactionTypeOptions) },
        modifier = modifier.fillMaxWidth()
    )
}

enum class TransactionTypeOptions(override val label: @Composable (() -> Unit)): SegmentOption {
    EXPENSE(
        label = {
            OptionText(
                text = "Expense",
                leadingIcon = {
                    Icon(
                        imageVector = TrendingDownIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ),
    INCOME(
        label = {
            OptionText(
                text = "Income",
                leadingIcon = {
                    Icon(
                        imageVector = TrendingUpIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ),
}

@Preview(showBackground = true)
@Composable
fun TransactionTypeSelectorPreview() {
    SpendLessAppTheme {
        TransactionTypeSelector(
            selectedOption = TransactionTypeOptions.EXPENSE,
            onOptionSelected = {}
        )
    }
}