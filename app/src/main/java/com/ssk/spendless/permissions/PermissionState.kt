package com.ssk.spendless.permissions

data class PermissionState(
    val notificationsPermission: Boolean = false,
    val scheduleExactAlarmPermission: Boolean = false,
    val permissionRequested: Map<Permissions, Boolean> = mapOf(
        Permissions.POST_NOTIFICATIONS to false,
        Permissions.SCHEDULE_EXACT_ALARM to false
    )
) {
    val allGranted: Boolean
        get() = notificationsPermission && scheduleExactAlarmPermission
}