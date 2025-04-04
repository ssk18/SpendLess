package com.ssk.auth.presentation.screens.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.auth.presentation.R
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText
import com.ssk.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvents>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPinChange -> {
                _state.update {
                    it.copy(
                        pin = TextFieldState(action.pin)
                    )
                }
            }
            is LoginAction.OnUserNameChange -> _state.update {
                it.copy(
                    username = TextFieldState(action.userName)
                )
            }

            LoginAction.OnLoginClick -> validateUser()
            LoginAction.OnRegisterClick -> navigateToRegisterScreen()
        }
    }

    fun validateUser() {
        viewModelScope.launch {
            val userName = _state.value.username.text.toString()
            val pin = _state.value.pin.text.toString()

            val isPinValid = userRepository.verifyPin(userName, pin)

            when (isPinValid) {
                is Result.Success -> {
                    _events.send(LoginEvents.ShowSnackbar(UiText.StringResource(R.string.login_successful)))
                    _state.update {
                        it.copy(
                            snackbarType = SnackbarType.Success
                        )
                    }
                }

                is Result.Error -> {
                    _events.send(LoginEvents.ShowSnackbar(isPinValid.error.asUiText()))
                    _state.update {
                        it.copy(
                            snackbarType = SnackbarType.Error
                        )
                    }
                }
            }
        }
    }

    private fun navigateToRegisterScreen() {
        viewModelScope.launch {
            _events.send(LoginEvents.NavigateToRegisterScreen)
        }
    }
}