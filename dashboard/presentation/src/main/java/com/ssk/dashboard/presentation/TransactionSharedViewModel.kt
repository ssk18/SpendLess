package com.ssk.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.User
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.ui.UiText
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionAction
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionEvent
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionState
import com.ssk.dashboard.presentation.dashboard.DashboardAction
import com.ssk.dashboard.presentation.dashboard.DashboardEvent
import com.ssk.dashboard.presentation.dashboard.DashboardState
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings
import com.ssk.dashboard.presentation.dashboard.utils.AmountFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionSharedViewModel(
    private val userRepository: IUserRepository,
    private val transactionRepository: ITransactionsRepository,
    private val sessionRepository: ISessionRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    private val _createTransactionState = MutableStateFlow(CreateTransactionState())
    val createTransactionState = _createTransactionState.asStateFlow()

    private val _transactionsEvent = Channel<CreateTransactionEvent>()
    val transactionsEvent = _transactionsEvent.receiveAsFlow()

    private val _dashboardEvent = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEvent.receiveAsFlow()

    private var userId: Long? = null

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.NavigateToAllTransactions -> TODO()
            is DashboardAction.NavigateToCreateTransaction -> {
                _dashboardState.update {
                    it.copy(showCreateTransactionSheet = true)
                }
            }

            DashboardAction.NavigateToPinPrompt -> TODO()
            DashboardAction.NavigateToSettings -> TODO()
            DashboardAction.OnShowAllTransactionsClicked -> TODO()
            is DashboardAction.UpdateExportBottomSheet -> {
                _dashboardState.update { currentState ->
                    currentState.copy(
                        showCreateTransactionSheet = action.showSheet
                    )
                }
            }
        }
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            CreateTransactionAction.OnBottomSheetCloseClicked -> {
                _dashboardState.update {
                    it.copy(showCreateTransactionSheet = false)
                }
            }

            CreateTransactionAction.OnCreateTransactionClicked -> TODO()
            is CreateTransactionAction.OnExpenseCategorySelected -> {
                _createTransactionState.update {
                    it.copy(
                        expenseCategory = action.category
                    )
                }
            }

            is CreateTransactionAction.OnRepeatingCategorySelected -> {
                _createTransactionState.update {
                    it.copy(
                        repeatingCategory = action.repeatingCategory
                    )
                }
            }

            is CreateTransactionAction.OnTransactionTypeSelected -> {
                _createTransactionState.update {
                    it.copy(
                        transactionType = action.transactionType
                    )
                }
            }
        }
    }

    private fun getDashBoardData() {
        viewModelScope.launch {
            val username = getUsername()
            _dashboardState.update { currentState ->
                currentState.copy(
                    username = username
                )
            }

            val userDetails = userRepository.getUser(username)
            when (userDetails) {
                is Result.Success -> {
                    sessionRepository.startSession(userDetails.data.settings.sessionExpiryDuration)
                    setAmountSettings(userDetails.data)
                    userId = userDetails.data.userId
                }

                is Result.Error -> {
                    _dashboardEvent.trySend(DashboardEvent.ShowSnackbar(UiText.DynamicString("Error fetching user details")))
                }
            }
        }
    }

    private fun getUsername(): String {
        return sessionRepository.getLoggedInUsername() ?: throw UserNotLoggedInException()
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

    private fun createTransaction() {
        userId?.let {
            val transactionFields = _createTransactionState.value.transactionFieldsState
            val transactionTitle = transactionFields.title.text.toString().trim()
            val transactionAmount = AmountFormatter.parseAmountToFloat(
                amount = transactionFields.amount.text,
                amountSettings = _dashboardState.value.amountSettings,
                isExpense = _createTransactionState.value.isExpense
            )
        }
    }

}