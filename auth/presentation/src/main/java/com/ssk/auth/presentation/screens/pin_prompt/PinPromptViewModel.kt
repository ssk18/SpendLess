package com.ssk.auth.presentation.screens.pin_prompt

import androidx.lifecycle.ViewModel
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository

class PinPromptViewModel(
    private val userRepository: IUserRepository,
    private val sessionRepository: ISessionRepository
): ViewModel() {

}