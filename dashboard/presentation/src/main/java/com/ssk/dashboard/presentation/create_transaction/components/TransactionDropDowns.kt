package com.ssk.dashboard.presentation.create_transaction.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.components.dropdown.DropDownSelector
import com.ssk.core.presentation.designsystem.model.RecurringTypeUI
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun TransactionDropDowns(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    selectedExpenseCategory: TransactionCategoryTypeUI,
    selectedRepeatType: RecurringTypeUI,
    onExpenseCategorySelected: (TransactionCategoryTypeUI) -> Unit,
    onRepeatTypeSelected: (RecurringTypeUI) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (isExpense) {
            DropDownSelector(
                options = TransactionCategoryTypeUI.expenseCategories().toList(),
                selectedOption = selectedExpenseCategory,
                onOptionSelected = onExpenseCategorySelected,
                currencyCodeShow = { it.symbol },
                currencyNameShow = { it.title },
                fontStyle = MaterialTheme.typography.bodyMedium,
                showIconBackground = true
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DropDownSelector(
            options = RecurringTypeUI.entries,
            selectedOption = selectedRepeatType,
            onOptionSelected = onRepeatTypeSelected,
            currencyCodeShow = { it.symbol },
            currencyNameShow = { it.title },
            fontStyle = MaterialTheme.typography.bodyMedium,
            showIconBackground = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionDropDownsPreview() {
    SpendLessAppTheme {
        TransactionDropDowns(
            isExpense = true,
            selectedExpenseCategory = TransactionCategoryTypeUI.TRANSPORTATION,
            selectedRepeatType = RecurringTypeUI.ONE_TIME,
            onExpenseCategorySelected = {},
            onRepeatTypeSelected = {}
        )
    }
}