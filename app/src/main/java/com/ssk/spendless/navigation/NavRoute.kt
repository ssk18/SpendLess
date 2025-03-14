package com.ssk.spendless.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object HomeRoot : NavRoute

    @Serializable
    data object Register : NavRoute

    @Serializable
    data class PinEntry(val username: String) : NavRoute
}