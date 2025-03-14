package com.ssk.spendless.auth.presentation.pinentryscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.spendless.R
import com.ssk.spendless.auth.domain.IUserRepository
import com.ssk.spendless.auth.domain.model.User
import com.ssk.spendless.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PinEntryViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val username = savedStateHandle.get<String>("username") ?: ""

    private val _state = MutableStateFlow(PinEntryState())
    val state: StateFlow<PinEntryState> = _state

    val _uiEvents = Channel<PinEntryEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: PinEntryAction) {
        when (action) {
            PinEntryAction.OnBackClick -> handleNavigateBack()
            PinEntryAction.OnDeleteClick -> handleDeleteDigit()
            is PinEntryAction.OnPinButtonClick -> handlePinEntry(action.pin)
            PinEntryAction.OnClearPin -> handleClearPin()
            PinEntryAction.OnSubmitPin -> handleSubmitPin()
        }
    }

    private fun handleDeleteDigit() {
        if (_state.value.pin.isNotEmpty()) {
            _state.update {
                it.copy(
                    pin = it.pin.dropLast(1),
                    error = null
                )
            }
        }
    }

    private fun handleClearPin() {
        _state.value = _state.value.copy(pin = "", error = null)
    }

    fun handlePinEntry(pin: String) {
        val currentState = _state.value
        if (currentState.pin.length < currentState.maxPinLength) {
            val newPin = currentState.pin + pin
            _state.update {
                it.copy(
                    pin = newPin,
                    error = null
                )
            }

            if (newPin.length == currentState.maxPinLength) {
                viewModelScope.launch {
                    handleSubmitPin()
                }
            }
        }
    }

    private fun handleSubmitPin() {
        val currentState = _state.value
        viewModelScope.launch {
            when (currentState.mode) {
                PinEntryMode.CREATE -> {
                    _state.update {
                        it.copy(
                            mode = PinEntryMode.CONFIRM,
                            initialPin = it.pin,
                            pin = ""
                        )
                    }
                }

                PinEntryMode.CONFIRM -> {
                    if (currentState.pin == currentState.initialPin) {
                        try {
                            userRepository.insertUser(
                                User(
                                    username = username,
                                    pinCode = currentState.pin.toInt()
                                )
                            )
                            _uiEvents.send(
                                PinEntryEvents.ShowSnackbar(
                                    message = UiText.StringResource(R.string.pin_created_successfully),
                                    type = SnackbarType.Success
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            _state.update { pinEntryState ->
                                pinEntryState.copy(
                                    isLoading = false,
                                    error = UiText.DynamicString(e.message ?: "Unknown Error")
                                )
                            }
                            _uiEvents.send(
                                PinEntryEvents.ShowSnackbar(
                                    UiText.DynamicString(
                                        e.message ?: "Unknown Error"
                                    ),
                                    SnackbarType.Error
                                )
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                error = UiText.StringResource(R.string.pins_do_not_match),
                                pin = ""
                            )
                        }
                        _uiEvents.send(PinEntryEvents.ShowPinShakeAnimation)
                        _uiEvents.send(
                            PinEntryEvents.ShowSnackbar(
                                UiText.StringResource(R.string.pins_do_not_match),
                                SnackbarType.Error
                            ),
                        )
                    }
                }
            }
        }
    }

    private fun handleNavigateBack() {
        val currentState = _state.value

        when (currentState.mode) {
            PinEntryMode.CREATE -> {
                viewModelScope.launch {
                    _uiEvents.trySend(PinEntryEvents.OnNavigateBack)
                }
            }

            PinEntryMode.CONFIRM -> {
                _state.update {
                    it.copy(
                        mode = PinEntryMode.CREATE,
                        pin = "",
                        error = null
                    )
                }
            }
        }
    }
}