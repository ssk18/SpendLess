package com.ssk.dashboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessFloatingActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.DownloadIcon
import com.ssk.core.presentation.designsystem.theme.SettingsIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.dashboard.presentation.dashboard.components.DashBoardContent
import com.ssk.dashboard.presentation.dashboard.components.EmptyTransactionView

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    SpendLessScaffold(
        applyGradient = true,
        topBar = {
            DashboardTopBar(
                state = state,
                onAction = onAction
            )
        },
        floatingActionButton = {
            SpendLessFloatingActionButton(
                onClick = {
                    onAction(DashboardAction.NavigateToCreateTransaction)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            Column {
                DashBoardContent(state = state)

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            ),
                            color = MaterialTheme.colorScheme.background
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        if (state.latestTransactions.isEmpty()) {
                            EmptyTransactionView()
                        } else {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardTopBar(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    AppTopBar(
        title = state.username,
        startIcon = null,
        endIcon1 = DownloadIcon,
        endIcon2 = SettingsIcon,
        onStartIconClick = null,
        onEndIcon1Click = {
            onAction(DashboardAction.UpdateExportBottomSheet(true))
        },
        onEndIcon2Click = {
            onAction(DashboardAction.NavigateToSettings)
        },
        modifier = Modifier.padding(
            horizontal = 8.dp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    SpendLessAppTheme {
        DashboardScreen(
            state = DashboardState(),
            onAction = {}
        )
    }
}