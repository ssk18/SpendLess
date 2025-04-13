package com.ssk.dashboard.presentation.dashboard.create_transaction

import androidx.compose.foundation.text.input.TextFieldState
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType
import com.ssk.dashboard.presentation.dashboard.create_transaction.components.TransactionTypeOptions

data class CreateTransactionState(
    val isCreateTransactionOpen: Boolean = false,
    val transactionType: TransactionTypeOptions = TransactionTypeOptions.EXPENSE,
    val transactionTitle: TextFieldState = TextFieldState(),
    val transactionAmount: TextFieldState = TextFieldState(),
    val transactionNote: TextFieldState = TextFieldState(),
    val expenseCategory: TransactionType = TransactionType.OTHER,
    val repeatingCategory: RepeatType = RepeatType.NOT_REPEAT
)
