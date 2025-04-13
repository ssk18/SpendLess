package com.ssk.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.User
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val sessionRepository: ISessionRepository,
    private val transactionsRepository: ITransactionsRepository,
    private val userRepository: IUserRepository
): ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    init {
        getDashboardData()
    }

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

    private fun getDashboardData() {
        viewModelScope.launch {
            val username = sessionRepository.getLoggedInUsername() ?: throw UserNotLoggedInException()
            updateUserName(username)

            val result = userRepository.getUser(username)
            when (result) {
                is Result.Success -> {
                    sessionRepository.startSession(result.data.settings.sessionExpiryDuration)
                    setAmountSettings(result.data)
                }
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    private fun updateUserName(username: String) {
        _dashboardState.update { currentState ->
            currentState.copy(
                username = username
            )
        }
    }

    private fun setAmountSettings(user: User) {
        _dashboardState.update { currentState ->
            currentState.copy(
                amountSettings = AmountSettings(
                    expensesFormat = currentState.amountSettings.expensesFormat,
                    currency = currentState.amountSettings.currency,
                    decimalSeparator = currentState.amountSettings.decimalSeparator,
                    thousandsSeparator = currentState.amountSettings.thousandsSeparator,
                )
            )
        }
    }

}