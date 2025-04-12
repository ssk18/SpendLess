package com.ssk.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.ITransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel(
    private val sessionRepository: ISessionRepository,
    private val transactionsRepository: ITransactionsRepository
): ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.NavigateToAllTransactions -> {
                // Handle navigation to all transactions
            }
            is DashboardAction.NavigateToPinPrompt -> {
                // Handle navigation to pin prompt
            }
            is DashboardAction.NavigateToSettings -> {
                // Handle navigation to settings
            }
            is DashboardAction.NavigateToCreateTransaction -> {
                // Handle navigation to create transaction
            }
            is DashboardAction.UpdateExportBottomSheet -> {
                // Handle update export bottom sheet
            }
            is DashboardAction.OnShowAllTransactionsClicked -> {
                // Handle show all transactions clicked
            }
        }
    }

}