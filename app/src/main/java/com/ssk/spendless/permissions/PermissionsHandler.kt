package com.ssk.spendless.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssk.spendless.MainViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsHandler(
    viewModel: MainViewModel,
    permissionState: PermissionState,
    context: Context
) {
    // Permission dialogs state
    val showAlarmPermissionDialog = remember { mutableStateOf(false) }
    
    // Notification permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Timber.d("Notification permission result: $isGranted")
            viewModel.onPermissionResult(
                permission = Permissions.POST_NOTIFICATIONS,
                isGranted = isGranted
            )
        }
    )
    
    // Check for permission changes whenever this composable is active
    DisposableEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
                val isAlarmPermissionGranted = alarmManager.canScheduleExactAlarms()
                Timber.d("Alarm permission check in DisposableEffect: $isAlarmPermissionGranted")
                viewModel.onPermissionResult(
                    permission = Permissions.SCHEDULE_EXACT_ALARM,
                    isGranted = isAlarmPermissionGranted
                )

                // If not granted and already requested, show dialog
                if (!isAlarmPermissionGranted &&
                    permissionState.permissionRequested[Permissions.SCHEDULE_EXACT_ALARM] == true) {
                    showAlarmPermissionDialog.value = true
                }
            } catch (e: Exception) {
                Timber.e(e, "Error checking alarm permission in DisposableEffect")
                viewModel.onPermissionResult(Permissions.SCHEDULE_EXACT_ALARM, false)
            }
        }

        onDispose { }
    }
    
    // Request permissions when needed
    LaunchedEffect(permissionState) {
        Timber.d("LaunchedEffect: Notification permission: ${permissionState.notificationsPermission}, " +
                "requested: ${permissionState.permissionRequested[Permissions.POST_NOTIFICATIONS]}")
        
        // Request notification permission
        if (!permissionState.notificationsPermission && 
            permissionState.permissionRequested[Permissions.POST_NOTIFICATIONS] == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Timber.d("Requesting notification permission")
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                viewModel.onPermissionRequested(Permissions.POST_NOTIFICATIONS)
            }
        }
        
        Timber.d("LaunchedEffect: Alarm permission: ${permissionState.scheduleExactAlarmPermission}, " +
                "requested: ${permissionState.permissionRequested[Permissions.SCHEDULE_EXACT_ALARM]}")
                
        // Show alarm permission dialog if needed
        if (!permissionState.scheduleExactAlarmPermission && 
            permissionState.permissionRequested[Permissions.SCHEDULE_EXACT_ALARM] == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Timber.d("Setting up alarm permission request")
                viewModel.onPermissionRequested(Permissions.SCHEDULE_EXACT_ALARM)
                showAlarmPermissionDialog.value = true
            }
        }
    }
    
    // Alarm permission dialog using BasicAlertDialog
    if (showAlarmPermissionDialog.value) {
        Timber.d("Showing alarm permission dialog")
        BasicAlertDialog(
            onDismissRequest = { /* Cannot dismiss by clicking outside */ }
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Alarm Permission Required")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "This app needs permission to schedule exact alarms for expense notifications. " +
                        "Please enable this permission in the upcoming settings screen."
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            Timber.d("Opening alarm permission settings")
                            showAlarmPermissionDialog.value = false
                            
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Text("Open Settings")
                    }
                }
            }
        }
    }
}