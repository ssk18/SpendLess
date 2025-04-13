package com.ssk.dashboard.presentation.create_transaction

import androidx.compose.foundation.text.input.TextFieldState
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeOptions

data class CreateTransactionState(
    val isCreateTransactionOpen: Boolean = false,
    val transactionType: TransactionTypeOptions = TransactionTypeOptions.EXPENSE,
    val transactionFieldsState: TransactionFieldsState = TransactionFieldsState(),
    val expenseCategory: TransactionType = TransactionType.OTHER,
    val repeatingCategory: RepeatType = RepeatType.NOT_REPEAT,
    val expensesFormat: ExpensesFormat = ExpensesFormat.MINUS
) {
    val isCreateButtonEnabled: Boolean
        get() {
            val hasCounterpartyMinLength = transactionFieldsState.title.text.length >= MIN_COUNTERPARTY_LENGTH
            val isAmountNotBlank = transactionFieldsState.amount.text.isNotBlank()
            return isAmountNotBlank && hasCounterpartyMinLength
        }

    val isExpense: Boolean
        get() {
            return transactionType == TransactionTypeOptions.EXPENSE
        }

    data class TransactionFieldsState(
        val title: TextFieldState = TextFieldState(),
        val amount: TextFieldState = TextFieldState(),
        val note: TextFieldState = TextFieldState(),
    )

    companion object {
        private const val MIN_COUNTERPARTY_LENGTH = 3
    }
}
