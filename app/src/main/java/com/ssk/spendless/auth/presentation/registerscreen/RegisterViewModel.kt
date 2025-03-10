@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.ssk.spendless.auth.presentation.registerscreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.spendless.auth.domain.UserDataValidator
import com.ssk.spendless.core.presentation.ui.textAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state
        .flatMapLatest { currentState ->
            currentState.username.textAsFlow()
                .map { textFieldValue ->
                    val username = textFieldValue.toString()
                    val validationResult = userDataValidator.validateUsername(username)

                    currentState.copy(
                        userNameValidationState = validationResult.isValid,
                        isButtonEnabled = validationResult.isValid
                    )
                }
                .onStart { emit (currentState) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RegisterState(username = TextFieldState())
        )


    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNextClick -> {
                // Handle next button click if needed
            }
            is RegisterAction.OnUserNameChange -> {
                _state.update {
                    it.copy(username = TextFieldState(action.username))
                }
            }
        }
    }
}