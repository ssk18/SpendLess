package com.ssk.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.spendless.MainAction
import com.ssk.spendless.MainEvent
import com.ssk.spendless.MainViewModel
import com.ssk.spendless.navigation.graphs.authGraph
import com.ssk.spendless.navigation.graphs.navigateToPinPrompt
import com.ssk.spendless.navigation.graphs.settingsGraph
import com.ssk.spendless.navigation.graphs.transactionsGraph
import com.ssk.spendless.navigation.routes.NavRoute
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SpendLessNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: NavRoute,
    sessionRepository: ISessionRepository
) {
    val isUserSessionExpired = remember {
        sessionRepository.isUserLoggedIn() && sessionRepository.isSessionExpired()
    }
    val mainViewModel = koinViewModel<MainViewModel>()

    // Track when navigating to PinPrompt
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            if (route?.contains("PinPrompt") == true) {
                mainViewModel.onAction(MainAction.SetPinPromptActive(true))
                Timber.d("Detected navigation to PIN prompt, disabling session checks")
            } else if (route != null) {
                // Reset flag when navigating away from PIN prompt
                mainViewModel.onAction(MainAction.SetPinPromptActive(false))
                Timber.d("Navigated away from PIN prompt: $route")
            }
        }
        
        navController.addOnDestinationChangedListener(listener)
        
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    ObserveAsEvents(mainViewModel.event) { event ->
        when (event) {
            MainEvent.SessionExpired -> {
                navController.navigateToPinPrompt()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authGraph(
            navController,
            isUserSessionExpired,
            modifier
        )
        transactionsGraph(
            navController = navController,
            modifier = modifier
        )
        settingsGraph(
            navController = navController,
            modifier = modifier
        )
    }
}

