package com.ssk.dashboard.presentation.all_transactions

import com.ssk.core.domain.model.Transaction
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings
import java.time.Instant

data class AllTransactionsUiState(
    val transactions: Map<Instant, List<Transaction>> = emptyMap(),
    val amountSettings: AmountSettings = AmountSettings(),
)
