package com.ssk.auth.presentation.screens.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.auth.presentation.R
import com.ssk.core.domain.repository.ISessionRepository
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
    private val userRepository: IUserRepository,
    private val sessionRepository: ISessionRepository
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
            
            try {
                // First check if user exists
                val userExists = userRepository.getUser(userName)
                
                when (userExists) {
                    is Result.Success -> {
                        // Now check pin
                        val user = userExists.data
                        
                        if (user.pinCode == pin) {
                            // Pin matches, login successful
                            sessionRepository.logIn(userName)
                            
                            _events.send(LoginEvents.ShowSnackbar(UiText.StringResource(R.string.login_successful)))
                            _events.send(LoginEvents.NavigateToDashboard)
                            _state.update {
                                it.copy(
                                    snackbarType = SnackbarType.Success
                                )
                            }
                        } else {
                            // Pin doesn't match
                            _events.send(LoginEvents.ShowSnackbar(UiText.StringResource(R.string.invalid_credentials)))
                            _state.update {
                                it.copy(
                                    snackbarType = SnackbarType.Error
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        // User doesn't exist
                        _events.send(LoginEvents.ShowSnackbar(UiText.StringResource(R.string.user_not_found)))
                        _state.update {
                            it.copy(
                                snackbarType = SnackbarType.Error
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("LoginViewModel", "Login error: ${e.message}", e)
                _events.send(LoginEvents.ShowSnackbar(UiText.StringResource(R.string.unknown_error)))
                _state.update {
                    it.copy(
                        snackbarType = SnackbarType.Error
                    )
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