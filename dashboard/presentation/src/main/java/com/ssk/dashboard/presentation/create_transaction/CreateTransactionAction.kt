package com.ssk.dashboard.presentation.create_transaction

import com.ssk.core.presentation.designsystem.model.RecurringTypeUI
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeOptions

sealed interface CreateTransactionAction {
    data class OnTransactionTypeSelected(val transactionType: TransactionTypeOptions) : CreateTransactionAction
    data class OnExpenseCategorySelected(val category: TransactionCategoryTypeUI) : CreateTransactionAction
    data class OnRepeatingCategorySelected(val repeatingCategory: RecurringTypeUI) : CreateTransactionAction
    data object OnCreateTransactionClicked : CreateTransactionAction
    data object OnBottomSheetCloseClicked : CreateTransactionAction
}