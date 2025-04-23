package com.ssk.auth.presentation.screens.pin_prompt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.auth.presentation.R
import com.ssk.core.domain.exception.UserNotLoggedInException
import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PinPromptViewModel(
    private val userRepository: IUserRepository,
    private val sessionRepository: ISessionRepository
) : ViewModel() {

    var state by mutableStateOf(PinPromptUiState())
        private set

    private val _events = Channel<PinPromptEvent>()
    val event = _events.receiveAsFlow()

    fun onAction(action: PinPromptUiAction) {
        when (action) {
            PinPromptUiAction.OnDeleteClick -> TODO()
            PinPromptUiAction.OnLogoutClick -> TODO()
            is PinPromptUiAction.OnPinButtonClick -> TODO()
            PinPromptUiAction.OnSubmitPin -> TODO()
        }
    }

    private fun checkPinLockStatus() {
        val pinLockDuration = sessionRepository.getPinLockRemainingTime()

    }

    private fun validatePin(enteredPin: String) {
        viewModelScope.launch {
            val username = getUsername()
            val result = userRepository.getUser(username)
            when (result) {
                is Result.Error  -> _events.send(PinPromptEvent.ShowSnackbar(
                    UiText.DynamicString(result.error.toString())
                ))
                is Result.Success -> {
                    val user =  result.data
                    val pin = user.pinCode
                    if (pin == enteredPin) {
                        sessionRepository.startSession(user.settings.sessionExpiryDuration)
                        _events.send(PinPromptEvent.OnSuccessfulPin)
                    } else {
                        state = state.copy(
                            pinCode = "",
                        )
                        _events.send(PinPromptEvent.ShowSnackbar(
                            UiText.StringResource(R.string.wrong_pin)
                        ))
                        updateUserAttempts()

                        if (state.currentAttempt == MAX_ATTEMPT) {
                            lockKeyboard(user.settings.lockedOutDuration)
                        }
                    }
                }
            }
        }
    }

    private fun getUsername(): String {
        return sessionRepository.getLoggedInUsername() ?: throw UserNotLoggedInException()
    }

    private fun lockKeyboard(lockedOutDuration: LockedOutDuration) {
        updateUserAttempts(resetPin = true)
        val duration = when (lockedOutDuration) {
            LockedOutDuration.FIFTEEN_SEC -> FIFTEEN_SECONDS
            LockedOutDuration.THIRTY_SEC -> THIRTY_SECONDS
            LockedOutDuration.ONE_MIN -> ONE_MINUTE
            LockedOutDuration.FIVE_MIN -> FIVE_MINUTES
        }
        sessionRepository.setPinLockTimestamp(lockedOutDuration)
        disableKeyboard(duration)
    }

    private fun disableKeyboard(lockedOutDuration: Int) {
        viewModelScope.launch {
            state = state.copy(
                isKeyboardLocked = true
            )
            for (i in lockedOutDuration downTo 0) {
                updateDescription(R.string.try_your_pin_again, i)
                if (i != 0) {
                    delay(1000)
                } else {
                    state = state.copy(
                        isKeyboardLocked = false
                    )
                    sessionRepository.restorePinLock()
                    updateDescription(R.string.enter_your_pin)
                }
            }
        }
    }

    private fun updateDescription(description: Int, vararg args: Any) {
        state = state.copy(
            description = UiText.StringResource(
                description,
                arrayOf(args)
            )
        )
    }

    private fun updateUserAttempts(resetPin: Boolean = false) {
        if (resetPin) {
            state = state.copy(
                currentAttempt = 0
            )
        } else {
            state = state.copy(
                currentAttempt = state.currentAttempt.inc()
            )
        }
    }

    companion object {
        private const val PIN_LENGTH = 5
        private const val MAX_ATTEMPT = 3
        private const val FIFTEEN_SECONDS = 15
        private const val THIRTY_SECONDS = 30
        private const val ONE_MINUTE = 60
        private const val FIVE_MINUTES = 300
        private const val PIN_FAILED_MESSAGE = "Too many failed attempts"
        private const val PIN_TRY_AGAIN = "Try your PIN again"
    }

}