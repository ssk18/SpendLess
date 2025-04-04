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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: IUserDataValidator,
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _events = Channel<RegisterEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.flatMapLatest { registerState ->
        checkUsernameValidity(registerState)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RegisterState()
        )

    private fun checkUsernameValidity(state: RegisterState): Flow<RegisterState> {
        return state.username.textAsFlow()
            .map { userName ->
                val isUserNameValid =
                    userDataValidator.validateUsername(userName.toString()).isValid
                if (isUserNameValid) {
                    state.copy(
                        isButtonEnabled = true,
                        userNameValidationState = true
                    )
                } else {
                    state.copy(
                        isButtonEnabled = false,
                        snackbarType = SnackbarType.Error,
                        userNameValidationState = false
                    )
                }
            }
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNextClick -> {
                viewModelScope.launch {
                    if (userRepository.getUser(action.username) is Result.Success) {
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