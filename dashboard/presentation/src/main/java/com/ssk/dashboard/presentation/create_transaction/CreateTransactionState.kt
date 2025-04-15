package com.ssk.dashboard.presentation.create_transaction

import androidx.compose.foundation.text.input.TextFieldState
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.presentation.designsystem.model.RecurringTypeUI
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ssk.dashboard.presentation.create_transaction.components.TransactionTypeOptions

data class CreateTransactionState(
    val isCreateTransactionOpen: Boolean = false,
    val transactionType: TransactionTypeOptions = TransactionTypeOptions.EXPENSE,
    val transactionFieldsState: TransactionFieldsState = TransactionFieldsState(),
    val expenseCategory: TransactionCategoryTypeUI = TransactionCategoryTypeUI.OTHER,
    val repeatingCategory: RecurringTypeUI = RecurringTypeUI.ONE_TIME,
    val expensesFormat: ExpensesFormat = ExpensesFormat.MINUS,
    val userId: Long? = null,
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
