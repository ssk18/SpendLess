package com.ssk.spendless.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.spendless.R
import com.ssk.spendless.auth.domain.UserDataValidator
import com.ssk.spendless.auth.domain.UsernameValidationError
import com.ssk.spendless.core.presentation.ui.UiText
import com.ssk.spendless.core.presentation.ui.textAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    var snackbarHostState = SnackbarHostState()
        private set

    init {
        state.username.textAsFlow().onEach { userName ->
            val validationResult = userDataValidator.validateUsername(userName.toString())
            state = state.copy(
                userNameValidationState = validationResult.isValid,
                isButtonEnabled = if (validationResult.isValid) {
                    true
                } else {
                    false
                },
                userNameValidationError = validationResult.error?.let { mapErrorToUiText(it) }
            )

            if (!validationResult.isValid) {
                snackbarHostState.showSnackbar(
                    message = validationResult.error.toString(),
                    withDismissAction = true
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNextClick -> {

            }

            is RegisterAction.OnUserNameChange -> {
                state = state.copy(username = TextFieldState(action.username))
            }
        }
    }

    private fun mapErrorToUiText(error: UsernameValidationError): UiText = when (error) {
        UsernameValidationError.EMPTY ->
            UiText.StringResource(R.string.username_cannot_be_empty)

        UsernameValidationError.TOO_SHORT ->
            UiText.StringResource(
                R.string.username_must_be_at_least_characters_long,
                arrayOf(UserDataValidator.MIN_USERNAME_LENGTH)
            )

        UsernameValidationError.TOO_LONG ->
            UiText.StringResource(
                R.string.username_must_be_at_max_characters_long,
                arrayOf(UserDataValidator.MAX_USERNAME_LENGTH)
            )

        UsernameValidationError.INVALID_CHARACTERS ->
            UiText.StringResource(R.string.username_can_contain_only_letters_numbers_and_underscores)
    }
}