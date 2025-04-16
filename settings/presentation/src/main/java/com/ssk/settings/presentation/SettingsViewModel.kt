package com.ssk.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.User
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.ui.components.toDomain
import com.ssk.settings.presentation.preferences.PreferenceUiAction
import com.ssk.settings.presentation.preferences.PreferenceUiEvent
import com.ssk.settings.presentation.preferences.PreferenceUiState
import com.ssk.settings.presentation.security.SecurityUiAction
import com.ssk.settings.presentation.security.SecurityUiState
import com.ssk.settings.presentation.security.components.toDomain
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

    private val _securityState = MutableStateFlow(SecurityUiState())
    val securityState = _securityState.asStateFlow()

    private val _preferencesState = MutableStateFlow(PreferenceUiState())
    val preferencesState = _preferencesState.asStateFlow()

    private val _preferencesEvent = Channel<PreferenceUiEvent>()
    val preferencesEvent = _preferencesEvent.receiveAsFlow()

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
                _securityState.update {
                    it.copy(
                        selectedBiometricsPrompt = action.biometricsPromptUi
                    )
                }
            }

            is SecurityUiAction.OnLockedOutDurationChanged -> {
                _securityState.update {
                    it.copy(
                        lockedOutDuration = action.lockedOutDurationUi
                    )
                }
            }

            is SecurityUiAction.OnSessionExpiryDurationChanged -> {
                _securityState.update {
                    it.copy(
                        selectedSessionExpiryDuration = action.sessionExpiryDurationUi
                    )
                }
            }

            SecurityUiAction.OnSaveClicked -> {
                saveSecuritySettings()
            }
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
            _preferencesEvent.send(PreferenceUiEvent.NavigateUp)
        }
    }

    private fun saveSecuritySettings() {
        viewModelScope.launch {
            val user = getUser()

            userRepository.upsertUser(
                user.copy(
                    settings = user.settings.copy(
                        biometricsPrompt = _securityState.value.selectedBiometricsPrompt.toDomain(),
                        sessionExpiryDuration = _securityState.value.selectedSessionExpiryDuration.toDomain(),
                        lockedOutDuration = _securityState.value.lockedOutDuration.toDomain()
                    )
                )
            )
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
}