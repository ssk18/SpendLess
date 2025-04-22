package com.ssk.spendless

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.spendless.navigation.routes.NavRoute
import com.ssk.spendless.permissions.PermissionState
import com.ssk.spendless.permissions.Permissions
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

    private val _permissionState = MutableStateFlow(PermissionState())
    val permissionState = _permissionState.asStateFlow()

    init {
        checkAuthState()
    }

    fun updatePermissionState(permission: Permissions, isGranted: Boolean) {
        _permissionState.update { currentState ->
            when (permission) {
                Permissions.POST_NOTIFICATIONS -> currentState.copy(
                    notificationsPermission = isGranted,
                )

                Permissions.SCHEDULE_EXACT_ALARM -> currentState.copy(
                    scheduleExactAlarmPermission = isGranted
                )
            }
        }
    }

    fun onPermissionResult(
        permission: Permissions,
        isGranted: Boolean
    ) {
        Timber.d("Permission result: $permission = $isGranted")
        updatePermissionState(permission, isGranted)
    }

    fun onPermissionRequested(permission: Permissions) {
        Timber.d("Permission requested: $permission")
        _permissionState.update { currentState ->
            currentState.copy(
                permissionRequested = currentState.permissionRequested.toMutableMap().apply {
                    this[permission] = true
                }
            )
        }
    }
    
    fun checkPermissionsAgain(context: Context) {
        Timber.d("Checking permissions again")
        // Implementation will be handled in MainActivity directly
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            val username = sessionRepository.getLoggedInUsername()
            val isLoggedIn = username != null

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