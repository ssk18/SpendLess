package com.ssk.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.User
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi
import com.ssk.core.presentation.ui.components.toDomain
import com.ssk.core.presentation.ui.states.ExpenseFormatState
import com.ssk.settings.presentation.preferences.PreferenceUiAction
import com.ssk.settings.presentation.preferences.PreferenceUiEvent
import com.ssk.settings.presentation.preferences.PreferenceUiState
import com.ssk.settings.presentation.security.SecurityUiAction
import com.ssk.settings.presentation.security.SecurityUiEvent
import com.ssk.settings.presentation.security.SecurityUiState
import com.ssk.settings.presentation.security.components.toDomain
import com.ssk.settings.presentation.security.components.toUi
import com.ssk.settings.presentation.settings.SettingsAction
import com.ssk.settings.presentation.settings.SettingsEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: IUserRepository,
    private val sessionRepository: ISessionRepository
) : ViewModel() {
    var securityState by mutableStateOf(SecurityUiState())
        private set

    private val _preferencesState = MutableStateFlow(PreferenceUiState())
    val preferencesState = _preferencesState.asStateFlow()

    private val _preferencesEvent = Channel<PreferenceUiEvent>()
    val preferencesEvent = _preferencesEvent.receiveAsFlow()

    private val _settingsEvent = Channel<SettingsEvent>()
    val settingsEvent = _settingsEvent.receiveAsFlow()

    private val _securityEvent = Channel<SecurityUiEvent>()
    val securityEvent = _securityEvent.receiveAsFlow()

    init {
        setUserSettings()
    }

    fun onAction(action: PreferenceUiAction) {
        when (action) {
            is PreferenceUiAction.OnCurrencySelected -> {
                _preferencesState.update { preferencesState ->
                    preferencesState.copy(
                        expenseFormatState = preferencesState.expenseFormatState.copy(
                            currency = action.currency
                        )
                    )
                }
            }

            is PreferenceUiAction.OnDecimalSeparatorClicked -> {
                _preferencesState.update { preferencesState ->
                    preferencesState.copy(
                        expenseFormatState = preferencesState.expenseFormatState.copy(
                            decimalSeparatorUi = action.decimalSeparator
                        )
                    )
                }
            }

            is PreferenceUiAction.OnExpenseFormatSelected -> {
                _preferencesState.update { preferencesState ->
                    preferencesState.copy(
                        expenseFormatState = preferencesState.expenseFormatState.copy(
                            expenseFormat = action.expenseFormat
                        )
                    )
                }
            }

            PreferenceUiAction.OnSaveClicked -> {
                saveUserPreferences()
            }

            is PreferenceUiAction.OnThousandsSeparatorClicked -> {
                _preferencesState.update { preferencesState ->
                    preferencesState.copy(
                        expenseFormatState = preferencesState.expenseFormatState.copy(
                            thousandsSeparatorUi = action.thousandsSeparator
                        )
                    )
                }
            }
        }
    }

    fun onAction(action: SecurityUiAction) {
        when (action) {
            is SecurityUiAction.OnBiometricsPromptChanged -> {
                securityState = securityState.copy(
                    selectedBiometricsPrompt = action.biometricsPromptUi
                )
            }

            is SecurityUiAction.OnLockedOutDurationChanged -> {
                securityState = securityState.copy(
                    lockedOutDuration = action.lockedOutDurationUi
                )
            }

            is SecurityUiAction.OnSessionExpiryDurationChanged -> {
                securityState = securityState.copy(
                    selectedSessionExpiryDuration = action.sessionExpiryDurationUi
                )
            }

            SecurityUiAction.OnSaveClicked -> {
                saveSecuritySettings()
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnPreferencesClicked -> {
                // Handled by composable directly
            }

            SettingsAction.OnSecurityClicked -> {
                // Handled by composable directly
            }

            SettingsAction.OnLogOutClicked -> {
                logOut()
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            sessionRepository.logOut()
            _settingsEvent.send(SettingsEvent.NavigateToLogin)
        }
    }

    private fun saveUserPreferences() {
        viewModelScope.launch {
            val user = getUser()

            val expenseFormatState = _preferencesState.value.expenseFormatState

            userRepository.upsertUser(
                user.copy(
                    settings = user.settings.copy(
                        expensesFormat = expenseFormatState.expenseFormat.toDomain(),
                        currency = expenseFormatState.currency,
                        decimalSeparator = expenseFormatState.decimalSeparatorUi.toDomain(),
                        thousandsSeparator = expenseFormatState.thousandsSeparatorUi.toDomain()
                    )
                )
            )
            if (sessionRepository.isSessionExpired()) {
                _preferencesEvent.send(PreferenceUiEvent.NavigateToPinPrompt)
            } else {
                sessionRepository.startSession(user.settings.sessionExpiryDuration)
                _preferencesEvent.send(PreferenceUiEvent.NavigateUp)
            }
        }
    }

    private fun saveSecuritySettings() {
        viewModelScope.launch {
            val user = getUser()

            userRepository.upsertUser(
                user.copy(
                    settings = user.settings.copy(
                        biometricsPrompt = securityState.selectedBiometricsPrompt.toDomain(),
                        sessionExpiryDuration = securityState.selectedSessionExpiryDuration.toDomain(),
                        lockedOutDuration = securityState.lockedOutDuration.toDomain()
                    )
                )
            )

            viewModelScope.launch {
                if (sessionRepository.isSessionExpired()) {
                    _securityEvent.send(SecurityUiEvent.NavigateToPinPrompt)
                } else {
                    sessionRepository.startSession(user.settings.sessionExpiryDuration)
                    _securityEvent.send(SecurityUiEvent.NavigateBack)
                }
            }
        }
    }

    private suspend fun getUser(): User {
        val username = sessionRepository.getLoggedInUsername() ?: throw UserNotLoggedInException()
        val result = userRepository.getUserAsFlow(username).firstOrNull()
            ?: throw IllegalArgumentException("Failed to load user data")

        return when (result) {
            is Result.Error -> throw IllegalArgumentException("User with username $username not found")
            is Result.Success -> result.data
        }
    }

    private fun setUserSettings() {
        viewModelScope.launch {
            val user = getUser()

            securityState = securityState.copy(
                selectedBiometricsPrompt = user.settings.biometricsPrompt.toUi(),
                selectedSessionExpiryDuration = user.settings.sessionExpiryDuration.toUi(),
                lockedOutDuration = user.settings.lockedOutDuration.toUi()
            )

            _preferencesState.update {
                it.copy(
                    expenseFormatState = ExpenseFormatState(
                        expenseFormat = ExpensesFormatUi.valueOf(user.settings.expensesFormat.name),
                        currency = user.settings.currency,
                        decimalSeparatorUi = DecimalSeparatorUi.valueOf(user.settings.decimalSeparator.name),
                        thousandsSeparatorUi = ThousandsSeparatorUi.valueOf(user.settings.thousandsSeparator.name)
                    ),

                )
            }
        }
    }
}