package com.ssk.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.spendless.navigation.routes.NavRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val sessionRepository: ISessionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            // Give splash screen a moment to show
            delay(300)
            
            val username = sessionRepository.getLoggedInUsername()
            val isLoggedIn = username != null

            Timber.d("User logged in status: $isLoggedIn")
            
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    isUserLoggedIn = isLoggedIn,
                    startDestination = if (isLoggedIn) NavRoute.TransactionsRoot else NavRoute.AuthRoot
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            sessionRepository.logOut()
            _state.update { currentState ->
                currentState.copy(
                    isUserLoggedIn = false,
                    startDestination = NavRoute.AuthRoot
                )
            }
        }
    }
}