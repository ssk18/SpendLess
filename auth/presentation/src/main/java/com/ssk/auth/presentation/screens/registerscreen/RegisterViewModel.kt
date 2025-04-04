@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.ssk.auth.presentation.screens.registerscreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.auth.domain.IUserDataValidator
import com.ssk.auth.presentation.R
import com.ssk.auth.presentation.screens.registerscreen.RegisterEvent.ShowSnackbar
import com.ssk.auth.presentation.screens.registerscreen.RegisterEvent.UsernameValid
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText.StringResource
import com.ssk.core.presentation.ui.textAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: IUserDataValidator,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _events = Channel<RegisterEvent>()
    val events = _events.receiveAsFlow()

    private val _duplicateUserNameValidation = MutableStateFlow<Boolean?>(null)

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    init {
        _state.value.username.textAsFlow()
            .onEach { userName ->
                val enteredUserName =
                    userDataValidator.validateUsername(userName.toString())

                if (enteredUserName.isValid) {
                    _state.update {
                        it.copy(
                            isButtonEnabled = true,
                            userNameValidationState = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isButtonEnabled = false,
                            snackbarType = SnackbarType.Error,
                            userNameValidationState = false
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

/*
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
*/

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNextClick -> {
                viewModelScope.launch {
                    if (userRepository.getUser(action.username) is Result.Success) {
                        _duplicateUserNameValidation.update { false }
                        _events.send(
                            ShowSnackbar(
                                message = StringResource(R.string.this_username_has_already_been_taken)
                            )
                        )
                        _state.update {
                            it.copy(
                                snackbarType = SnackbarType.Error,
                                userNameValidationState = false
                            )
                        }
                    } else {
                        _duplicateUserNameValidation.update { null }
                        _events.send(UsernameValid(action.username))
                    }
                }
            }

            is RegisterAction.OnUserNameChange -> {
                viewModelScope.launch {
                    val validationResult = userDataValidator.validateUsername(action.username)
                    _state.update {
                        it.copy(
                            username = TextFieldState(action.username),
                            isButtonEnabled = validationResult.isValid,
                            userNameValidationState = validationResult.isValid
                        )
                    }
                }
            }

            is RegisterAction.OnAlreadyHaveAnAccountClicked -> navigateToLoginScreen()
        }
    }

    private fun navigateToLoginScreen() {
        viewModelScope.launch {
            _events.send(RegisterEvent.NavigateToLoginScreen)
        }
    }
}