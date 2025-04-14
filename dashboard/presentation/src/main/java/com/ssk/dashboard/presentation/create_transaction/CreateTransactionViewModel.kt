package com.ssk.dashboard.presentation.create_transaction

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.dashboard.presentation.dashboard.utils.AmountFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTransactionViewModel(
    private val userRepository: IUserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateTransactionState())
    val uiState: StateFlow<CreateTransactionState> = _uiState.asStateFlow()

    private val _event = Channel<CreateTransactionEvent>()
    val event = _event.receiveAsFlow()

    fun setUserId(userId: Long) {
        _uiState.update { it.copy(userId = userId) }
        loadUserPreferences(userId)
    }

    private fun loadUserPreferences(userId: Long) {
        viewModelScope.launch {
            try {
                val result = userRepository.getUserById(userId)
                Log.d("CreateTransactionVM", "User preferences: $result")
                when (result) {
                    is Result.Success -> {
                        val user = result.data
                        _uiState.update { state ->
                            state.copy(
                                expensesFormat = user.settings.expensesFormat ?: ExpensesFormat.MINUS
                            )
                        }
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            CreateTransactionAction.OnCreateTransactionClicked -> {
                // Create transaction using the current state and userId
                // Implement transaction creation
            }
            is CreateTransactionAction.OnExpenseCategorySelected -> {
                _uiState.update {
                    it.copy(
                        expenseCategory = action.category
                    )
                }
            }
            is CreateTransactionAction.OnRepeatingCategorySelected -> {
                _uiState.update {
                    it.copy(
                        repeatingCategory = action.repeatingCategory
                    )
                }
            }
            is CreateTransactionAction.OnTransactionAmountChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        transactionFieldsState = currentState.transactionFieldsState.copy(
                            amount = TextFieldState(action.amount)
                        )
                    )
                }
            }
            is CreateTransactionAction.OnTransactionNoteChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        transactionFieldsState = currentState.transactionFieldsState.copy(
                            note = TextFieldState(action.note)
                        )
                    )
                }
            }
            is CreateTransactionAction.OnTransactionTitleChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        transactionFieldsState = currentState.transactionFieldsState.copy(
                            title = TextFieldState(action.title)
                        )
                    )
                }
            }
            is CreateTransactionAction.OnTransactionTypeSelected -> {
                _uiState.update {
                    it.copy(
                        transactionType = action.transactionType
                    )
                }
            }
            CreateTransactionAction.OnBottomSheetCloseClicked -> {
                _event.trySend(CreateTransactionEvent.CloseBottomSheet)
            }
        }
    }

    private fun createTransaction() {
        _uiState.value.userId?.let {
            val transactionFields = _uiState.value.transactionFieldsState
            val transactionTitle = transactionFields.title.text.toString().trim()
            val transactionAmount = AmountFormatter.getFormatedAmount(
                amount = transactionFields.amount.text,
                amountSettings = _uiState.value.amountSettings
            )
        }
    }
}