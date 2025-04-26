package com.ssk.spendless

import com.ssk.spendless.navigation.routes.NavRoute

data class MainState(
    val isLoading: Boolean = true,
    val startDestination: NavRoute = NavRoute.AuthRoot,
    val isUserLoggedIn: Boolean = false
)

sealed class MainEvent {
    data object SessionExpired : MainEvent()
}