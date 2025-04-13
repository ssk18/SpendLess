package com.ssk.dashboard.presentation.dashboard

sealed interface DashboardAction {
    data object NavigateToAllTransactions : DashboardAction
    data object NavigateToPinPrompt : DashboardAction
    data object NavigateToSettings : DashboardAction
    data class NavigateToCreateTransaction(val userId: Long) : DashboardAction
    data class UpdateExportBottomSheet(val showSheet: Boolean) : DashboardAction
    data object OnShowAllTransactionsClicked : DashboardAction
}