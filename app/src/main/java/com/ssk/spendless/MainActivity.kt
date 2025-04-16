package com.ssk.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.spendless.navigation.SpendLessNavigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val sessionRepository: ISessionRepository by inject()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen and keep it visible while loading
        val splashScreen = installSplashScreen()
        splashScreen.apply {
            setKeepOnScreenCondition {
                mainViewModel.state.value.isLoading
            }
        }
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val state = mainViewModel.state.collectAsStateWithLifecycle().value
            
            Timber.d("Navigation state: isLoggedIn=${state.isUserLoggedIn}, startDestination=${state.startDestination}")
            
            SpendLessAppTheme {
                SpendLessNavigation(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = state.startDestination
                )
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        sessionRepository.refreshSession()
    }
}