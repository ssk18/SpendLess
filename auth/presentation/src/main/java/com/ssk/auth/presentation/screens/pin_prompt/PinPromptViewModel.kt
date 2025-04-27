package com.ssk.auth.presentation.screens.pin_prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.auth.presentation.R
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.ui.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PinPromptViewModel(
    private val userRepository: IUserRepository,
    private val sessionRepository: ISessionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PinPromptUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<PinPromptEvent>()
    val event = _events.receiveAsFlow()

    fun onAction(action: PinPromptUiAction) {
        when (action) {
            PinPromptUiAction.OnDeleteClick -> deletePin()
            PinPromptUiAction.OnLogoutClick -> {
                viewModelScope.launch {
                    sessionRepository.logOut()
                    _events.send(PinPromptEvent.NavigateToLogin)
                }
            }

            is PinPromptUiAction.OnPinButtonClick -> {
                Timber.d("OnPinButtonClick: attempting to add digit ${action.pin}, current state: isExceededFailedAttempts=${_state.value.isExceededFailedAttempts}, current pin='${_state.value.pinCode}'")
                
                val pin = _state.value.pinCode
                if (pin.length < MAX_PIN_LENGTH) {
                    val newPin = pin + action.pin
                    Timber.d("Adding digit: current='$pin' + '${action.pin}' = newPin='$newPin'")
                    
                    _state.update {
                        it.copy(
                            pinCode = newPin,
                        )
                    }
                    
                    Timber.d("PIN digit added, expected new pin='$newPin', actual pin='${_state.value.pinCode}', new length: ${_state.value.pinCode.length}")
                }
                
                val updatedPin = _state.value.pinCode
                Timber.d("After update, pin='$updatedPin', length=${updatedPin.length}")
                
                if (updatedPin.length == MAX_PIN_LENGTH) {
                    Timber.d("PIN complete, validating...")
                    validatePin(updatedPin)
                }
            }
        }
    }

    init {
        Timber.d("PinPromptViewModel initialized, default state: isExceededFailedAttempts=${_state.value.isExceededFailedAttempts}")
        getUserSettings()
    }

    fun getUserSettings() {
        val user = getUserName()
        userRepository.getUserAsFlow(user)
            .onEach { userPreferences ->
                when (userPreferences) {
                    is Result.Error -> Unit
                    is Result.Success -> {
                        Timber.d("User settings: ${userPreferences.data}")
                        _state.update {
                            it.copy(
                                username = userPreferences.data.username,
                                lockoutDuration = userPreferences.data.settings.lockedOutDuration,
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getLockedoutDuration(lockedOutDuration: LockedOutDuration): Long {
        return when (lockedOutDuration) {
            LockedOutDuration.FIFTEEN_SEC -> 15
            LockedOutDuration.THIRTY_SEC -> 30
            LockedOutDuration.ONE_MIN -> 60
            LockedOutDuration.FIVE_MIN -> 300
        }
    }

    private fun deletePin() {
        _state.update {
            it.copy(
                pinCode = it.pinCode.dropLast(1)
            )
        }
    }

    private fun validatePin(enteredPin: String) {
        viewModelScope.launch {
            val username = getUserName()
            val user = userRepository.getUser(username)
            val userPin = withContext(Dispatchers.IO) {
                when (user) {
                    is Result.Error -> null
                    is Result.Success -> user.data.pinCode
                }
            }

            if (user is Result.Success && userPin == enteredPin) {
                sessionRepository.startSession(user.data.settings.sessionExpiryDuration)
                _events.send(PinPromptEvent.OnSuccessfulPin)
                return@launch
            }

            _state.update {
                it.copy(
                    pinCode = "",
                    remainingPinAttempts = it.remainingPinAttempts - 1
                )
            }

            if (_state.value.remainingPinAttempts <= 0) {
                startLockoutTimer()
            }

            _events.send(PinPromptEvent.ShowSnackbar(UiText.StringResource(R.string.wrong_pin)))
        }
    }

    private fun getUserName(): String {
        return sessionRepository.getLoggedInUsername() ?: throw UserNotLoggedInException()
    }

    private fun startLockoutTimer() {
        viewModelScope.launch {
            val lockoutDuration = getLockedoutDuration(_state.value.lockoutDuration)

            (lockoutDuration downTo 0).asFlow()
                .onEach { remainingTime ->
                    _state.update {
                        it.copy(
                            lockOutTimeRemaining = remainingTime,
                            pinCode = "",
                            isExceededFailedAttempts = true
                        )
                    }
                    delay(1000)
                }
                .onCompletion {
                    _state.update {
                        it.copy(
                            isExceededFailedAttempts = false,
                            remainingPinAttempts = MAX_ATTEMPT,
                            lockOutTimeRemaining = 0
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    companion object {
        const val MAX_PIN_LENGTH = 5
        const val MAX_ATTEMPT = 3
    }

}