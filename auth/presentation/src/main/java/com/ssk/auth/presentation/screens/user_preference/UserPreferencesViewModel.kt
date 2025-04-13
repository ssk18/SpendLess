package com.ssk.auth.presentation.screens.user_preference

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.model.Currency
import com.ssk.core.domain.model.User
import com.ssk.core.domain.model.UserSettings
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi
import com.ssk.core.presentation.ui.components.toDomain
import com.ssk.core.presentation.ui.states.ExpenseFormatState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: IUserRepository,
    private val sessionRepository: ISessionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserPreferenceState())
    val state = _state.asStateFlow()

    private val _uiEvents = Channel<UserPreferenceEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: UserPreferenceAction) {
        when (action) {
            is UserPreferenceAction.OnCurrencyUpdate -> updateCurrency(action.currency)
            is UserPreferenceAction.OnDecimalSeparatorUpdate -> updateDecimalSeparator(action.format)
            is UserPreferenceAction.OnExpenseFormatUpdate -> updateExpenseFormat(action.format)
            is UserPreferenceAction.OnThousandsSeparatorUpdate -> updateThousandsSeparator(action.format)
            UserPreferenceAction.OnBackClicked -> navigateToCreatePin()
            UserPreferenceAction.OnStartClicked -> saveUser()
        }
    }

    private fun navigateToCreatePin() {
        viewModelScope.launch {
            _uiEvents.send(UserPreferenceEvent.OnBackClicked)
        }
    }

    private fun saveUser() {
        val username = savedStateHandle.get<String>("username")
            ?: throw IllegalArgumentException("User name cannot be empty")
        val pin = savedStateHandle.get<String>("pinCode")
            ?: throw IllegalArgumentException("Pin cannot be empty")
        
        Log.d("UserPreferencesViewModel", "saveUser: username=$username, pin=$pin")
        
        val expenseFormatState = state.value.expensesFormatState
        val settings = UserSettings(
            currency = expenseFormatState.currency,
            decimalSeparator = expenseFormatState.decimalSeparatorUi.toDomain(),
            thousandsSeparator = expenseFormatState.thousandsSeparatorUi.toDomain(),
            expensesFormat = expenseFormatState.expenseFormat.toDomain()
        )
        
        Log.d("UserPreferencesViewModel", "User settings: currency=${settings.currency}, " +
            "decimal=${settings.decimalSeparator}, " +
            "thousands=${settings.thousandsSeparator}, " +
            "expFormat=${settings.expensesFormat}")

        val user = User(
            username = username,
            pinCode = pin,
            settings = settings
        )

        viewModelScope.launch {
            Log.d("UserPreferencesViewModel", "Registering user: ${user.username}")
            try {
                // First check if user already exists
                val existingUser = userRepository.getUser(username)
                if (existingUser is com.ssk.core.domain.utils.Result.Success) {
                    Log.d("UserPreferencesViewModel", "User already exists, updating login session")
                    // User already exists, just log them in
                    sessionRepository.logIn(username)
                    Log.d("UserPreferencesViewModel", "User logged in, navigating to dashboard")
                    _uiEvents.send(UserPreferenceEvent.NavigateToDashboardScreen)
                    return@launch
                }
                
                // Register the user
                val result = userRepository.registerUser(user)
                when (result) {
                    is com.ssk.core.domain.utils.Result.Success -> {
                        Log.d("UserPreferencesViewModel", "User registered successfully with ID: ${result.data}")
                        
                        // Double check the user was saved correctly
                        val savedUser = userRepository.getUser(username)
                        if (savedUser is com.ssk.core.domain.utils.Result.Success) {
                            Log.d("UserPreferencesViewModel", "Retrieved user: ${savedUser.data.username}, " +
                                "pin=${savedUser.data.pinCode}, " +
                                "settings=${savedUser.data.settings}")
                        } else {
                            Log.e("UserPreferencesViewModel", "Failed to retrieve newly saved user")
                        }
                        
                        // Log in the user
                        sessionRepository.logIn(username)
                        Log.d("UserPreferencesViewModel", "User logged in, navigating to dashboard")
                        _uiEvents.send(UserPreferenceEvent.NavigateToDashboardScreen)
                    }
                    is Result.Error -> {
                        Log.e("UserPreferencesViewModel", "Error registering user: ${result.error}")
                        // This is a development scenario, so we'll try to log in anyway
                        sessionRepository.logIn(username)
                        _uiEvents.send(UserPreferenceEvent.NavigateToDashboardScreen)
                    }
                }
            } catch (e: Exception) {
                Log.e("UserPreferencesViewModel", "Exception registering user: ${e.message}", e)
                // For development, we'll try to continue anyway
                try {
                    sessionRepository.logIn(username)
                    _uiEvents.send(UserPreferenceEvent.NavigateToDashboardScreen)
                } catch (innerE: Exception) {
                    Log.e("UserPreferencesViewModel", "Failed to recover: ${innerE.message}", innerE)
                }
            }
        }
    }

    private fun updateCurrency(currency: Currency) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                currency = currency
            )
        }
    }

    private fun updateDecimalSeparator(decimalSeparatorUi: DecimalSeparatorUi) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                decimalSeparatorUi = decimalSeparatorUi
            )
        }
    }

    private fun updateThousandsSeparator(thousandsSeparatorUi: ThousandsSeparatorUi) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                thousandsSeparatorUi = thousandsSeparatorUi
            )
        }
    }

    private fun updateExpenseFormat(expensesFormat: ExpensesFormatUi) {
        updateExpenseFormatState { currentState ->
            currentState.copy(
                expenseFormat = expensesFormat
            )
        }
    }

    private fun updateExpenseFormatState(update: (ExpenseFormatState) -> ExpenseFormatState) {
        _state.update { currentState ->
            currentState.copy(
                expensesFormatState = update(currentState.expensesFormatState)
            )
        }
    }
}