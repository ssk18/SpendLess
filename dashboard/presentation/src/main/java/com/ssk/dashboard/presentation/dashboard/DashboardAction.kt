package com.ssk.dashboard.presentation.dashboard

sealed interface DashboardAction {
    data object NavigateToPinPrompt : DashboardAction
    data object NavigateToSettings : DashboardAction
    data object NavigateToCreateTransaction : DashboardAction
    data class UpdateExportBottomSheet(val showSheet: Boolean) : DashboardAction
    data object OnShowAllTransactionsClicked : DashboardAction
    data object NavigateToExport : DashboardAction
}