package com.ssk.dashboard.presentation.create_transaction

import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeOptions

sealed interface CreateTransactionAction {
    data class OnTransactionTypeSelected(val transactionType: TransactionTypeOptions) : CreateTransactionAction
    data class OnExpenseCategorySelected(val category: TransactionType) : CreateTransactionAction
    data class OnRepeatingCategorySelected(val repeatingCategory: RepeatType) : CreateTransactionAction
    data object OnCreateTransactionClicked : CreateTransactionAction
    data object OnBottomSheetCloseClicked : CreateTransactionAction
    data class OnTransactionAmountChanged(val amount: String) : CreateTransactionAction
    data class OnTransactionTitleChanged(val title: String) : CreateTransactionAction
    data class OnTransactionNoteChanged(val note: String) : CreateTransactionAction
}