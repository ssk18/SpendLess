@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.ssk.auth.presentation.screens.registerscreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.auth.domain.IUserDataValidator
import com.ssk.auth.presentation.R
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText
import com.ssk.core.presentation.ui.textAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: IUserDataValidator,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val uiEvents = Channel<RegisterEvent>()
    val events = uiEvents.receiveAsFlow()

    private val _duplicateUserNameValidation = MutableStateFlow<Boolean?>(null)

    private val _state = MutableStateFlow(RegisterState())

    val state: StateFlow<RegisterState> = _state
        .flatMapLatest { baseState ->
            baseState.username.textAsFlow()
                .map { textFieldValue ->
                    val text = textFieldValue.toString()

                    // Clear validation override on text change
                    _duplicateUserNameValidation.tryEmit(null)

                    // Normal validation result
                    val isValid = userDataValidator.validateUsername(text).isValid

                    // Return updated state
                    baseState.copy(
                        userNameValidationState = isValid,
                        isButtonEnabled = isValid
                    )
                }
                .onStart { emit(baseState) }
        }
        // Apply validation override if present
        .combine(_duplicateUserNameValidation) { currentState, override ->
            if (override != null) {
                currentState.copy(
                    userNameValidationState = override,
                    isButtonEnabled = override
                )
            } else {
                currentState
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RegisterState()
        )

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNextClick -> {
                viewModelScope.launch {
                    if (userRepository.getUser(action.username) is Result.Success) {
                        _duplicateUserNameValidation.update { false }
                        uiEvents.send(
                            RegisterEvent.ShowSnackbar(
                                message = UiText.StringResource(R.string.this_username_has_already_been_taken)
                            )
                        )
                        _state.update {
                            it.copy(
                                userNameValidationState = false,
                                snackbarType = SnackbarType.Error
                            )
                        }
                    } else {
                        _duplicateUserNameValidation.update { null }
                        uiEvents.send(RegisterEvent.UsernameValid(action.username))
                    }
                }
            }

            is RegisterAction.OnUserNameChange -> {
                viewModelScope.launch {
                    val validationResult = userDataValidator.validateUsername(action.username)
                    _state.update {
                        it.copy(
                            username = TextFieldState(action.username),
                            userNameValidationState = validationResult.isValid,
                            isButtonEnabled = validationResult.isValid
                        )
                    }
                }
            }

            else -> {}
        }
    }
}