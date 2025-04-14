package com.ssk.dashboard.presentation.dashboard

import com.ssk.core.presentation.ui.UiText

sealed interface DashboardEvent {
    data class ShowSnackbar(
        val message: UiText
    ): DashboardEvent
}