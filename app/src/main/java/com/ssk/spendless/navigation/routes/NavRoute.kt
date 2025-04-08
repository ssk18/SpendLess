package com.ssk.spendless.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute {
    @Serializable
    data object AuthRoot : NavRoute

    @Serializable
    data object Register : NavRoute

    @Serializable
    data class PinEntry(val username: String) : NavRoute

    @Serializable
    data object Login : NavRoute

    @Serializable
    data class UserPreferences(val username: String, val pinCode: String) : NavRoute
}