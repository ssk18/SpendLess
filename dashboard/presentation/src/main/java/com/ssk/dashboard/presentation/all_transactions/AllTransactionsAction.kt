package com.ssk.dashboard.presentation.all_transactions

sealed interface AllTransactionsAction {
    data object OnExportClicked : AllTransactionsAction
}