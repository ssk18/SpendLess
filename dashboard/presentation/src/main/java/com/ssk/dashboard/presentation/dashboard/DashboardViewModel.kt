package com.ssk.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.ssk.core.domain.repository.ISessionRepository

class DashboardViewModel(
    private val sessionRepository: ISessionRepository,
): ViewModel() {
}