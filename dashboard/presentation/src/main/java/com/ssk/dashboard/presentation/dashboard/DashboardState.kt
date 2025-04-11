package com.ssk.dashboard.presentation.dashboard

import com.ssk.core.domain.model.Currency
import com.ssk.core.domain.model.Transaction
import com.ssk.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi
import java.time.Instant

data class DashboardState(
    val isDataLoaded: Boolean = false,
    val username: String = "",
    val accountInfoState: AccountInfoState = AccountInfoState(),
    val latestTransactions: Map<Instant, List<Transaction>> = emptyMap(),
    val amountSettings: AmountSettings = AmountSettings()
) {
    data class AccountInfoState(
        val accountBalance: String = "",
        val popularCategory: TransactionCategoryTypeUI? = null,
        val largestTransaction: LargestTransaction? = null,
        val previousWeekExpenseAmount: String = ""
    )

    data class LargestTransaction(
        val name: String = "",
        val amount: String = "",
        val date: String = ""
    )

    data class AmountSettings(
        val expensesFormat: ExpensesFormatUi = ExpensesFormatUi.MINUS,
        val currency: Currency = Currency.USD,
        val decimalSeparator: DecimalSeparatorUi = DecimalSeparatorUi.DOT,
        val thousandsSeparator: ThousandsSeparatorUi = ThousandsSeparatorUi.COMMA,
    )
}
