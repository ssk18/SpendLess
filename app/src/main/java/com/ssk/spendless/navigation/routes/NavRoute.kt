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
    data object PinPrompt : NavRoute

    @Serializable
    data class UserPreferences(val username: String, val pinCode: String) : NavRoute

    @Serializable
    data object TransactionsRoot : NavRoute

    @Serializable
    data object Dashboard : NavRoute

    @Serializable
    data object AllTransactions : NavRoute

    @Serializable
    data object SettingsRoot: NavRoute

    @Serializable
    data object Settings: NavRoute

    @Serializable
    data object Preferences: NavRoute

    @Serializable
    data object Security: NavRoute
}