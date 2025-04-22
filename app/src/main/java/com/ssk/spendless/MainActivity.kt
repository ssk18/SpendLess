package com.ssk.spendless

import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.dashboard.domain.ExpenseAlarmScheduler
import com.ssk.spendless.navigation.SpendLessNavigation
import com.ssk.spendless.navigation.routes.NavRoute
import com.ssk.spendless.permissions.Permissions
import com.ssk.spendless.permissions.PermissionsHandler
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val sessionRepository: ISessionRepository by inject()
    private val mainViewModel: MainViewModel by viewModel()
    private val expenseAlarmScheduler: ExpenseAlarmScheduler by inject()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.apply {
            setKeepOnScreenCondition {
                mainViewModel.state.value.isLoading
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRequestPermissions()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.permissionState.map {
                    it.allGranted
                }
                    .collect { allGranted ->
                        if (allGranted) {
                            expenseAlarmScheduler.scheduleExpenseCheck()
                        }
                    }
            }
        }

        val navigateTo = intent?.getStringExtra(EXTRA_NAVIGATION_DESTINATION)

        setContent {
            val navController = rememberNavController()
            val state = mainViewModel.state.collectAsStateWithLifecycle().value
            val permissionState = mainViewModel.permissionState.collectAsStateWithLifecycle().value

            PermissionsHandler(
                viewModel = mainViewModel,
                permissionState = permissionState,
                context = this@MainActivity
            )

            LaunchedEffect(navigateTo) {
                if (navigateTo == DESTINATION_ALL_TRANSACTIONS) {
                    navController.navigate(NavRoute.AllTransactions) {
                        popUpTo(NavRoute.Dashboard)
                    }
                }
            }

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
    
    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
        Timber.d("Permissions rechecked in onResume")
    }


    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            mainViewModel.updatePermissionState(
                Permissions.POST_NOTIFICATIONS,
                notificationPermission
            )
            Timber.d("Notification permission check: $notificationPermission")
        } else {
            mainViewModel.updatePermissionState(Permissions.POST_NOTIFICATIONS, true)
            Timber.d("Notification permission auto-granted (pre-Tiramisu)")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val scheduleExactAlarmPermission = alarmManager.canScheduleExactAlarms()
                mainViewModel.updatePermissionState(Permissions.SCHEDULE_EXACT_ALARM, scheduleExactAlarmPermission)
                Timber.d("Alarm permission check: $scheduleExactAlarmPermission")
            } catch (e: Exception) {
                Timber.e(e, "Error checking alarm permission")
                mainViewModel.updatePermissionState(Permissions.SCHEDULE_EXACT_ALARM, false)
            }
        } else {
            mainViewModel.updatePermissionState(Permissions.SCHEDULE_EXACT_ALARM, true)
            Timber.d("Alarm permission auto-granted (pre-Android S)")
        }
    }

    companion object {
        // Navigation constants exposed for notifications
        const val EXTRA_NAVIGATION_DESTINATION = "navigation_destination"
        const val DESTINATION_ALL_TRANSACTIONS = "all_transactions"
    }
}