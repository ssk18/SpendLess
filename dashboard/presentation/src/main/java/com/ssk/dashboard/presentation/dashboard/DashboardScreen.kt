package com.ssk.dashboard.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessFloatingActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.DownloadIcon
import com.ssk.core.presentation.designsystem.theme.SettingsIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.dashboard.presentation.TransactionSharedViewModel
import com.ssk.dashboard.presentation.create_transaction.CreateTransactionScreen
import com.ssk.dashboard.presentation.dashboard.components.DashBoardContent
import com.ssk.dashboard.presentation.dashboard.components.LatestTransactionView
import org.koin.androidx.compose.koinViewModel
import java.time.Instant

@Composable
fun DashboardScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: TransactionSharedViewModel = koinViewModel(),
    navigateToSettings: () -> Unit,
) {
    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val createTransactionState by viewModel.createTransactionState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.dashboardEvent) { event ->
        when (event) {
            DashboardEvent.NavigateToSettings -> {
                navigateToSettings()
            }
            is DashboardEvent.ShowSnackbar -> {

            }
        }
    }

    DashboardScreen(
        modifier = modifier,
        state = dashboardState,
        onAction = viewModel::onAction
    )

    if (dashboardState.showCreateTransactionSheet) {
        CreateTransactionScreen(
            state = createTransactionState,
            onAction = viewModel::onAction
        )
    }
}

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
                navigateToSettings = {
                    onAction(DashboardAction.NavigateToSettings)
                },
                navigateToExport = {
                    onAction(DashboardAction.NavigateToSettings)
                }
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
                DashBoardContent(
                    state = state,
                    modifier = Modifier
                        .weight(1f)
                )

                LatestTransactionView(
                    transactions = state.latestTransactions,
                    amountSettings = state.amountSettings,
                    onShowAllClick = {
                        onAction(DashboardAction.NavigateToAllTransactions)
                    },
                    modifier = Modifier.weight(1.4f)
                )
            }
        }
    }
}

@Composable
fun DashboardTopBar(
    state: DashboardState,
    navigateToSettings: () -> Unit,
    navigateToExport: () -> Unit,
) {
    AppTopBar(
        title = state.username,
        startIcon = null,
        endIcon1 = DownloadIcon,
        endIcon2 = SettingsIcon,
        onStartIconClick = null,
        onEndIcon1Click = {
            navigateToExport()
        },
        onEndIcon2Click = {
            navigateToSettings()
        },
        endIcon1BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
        endIcon1Color = MaterialTheme.colorScheme.onPrimary,
        endIcon2BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
        endIcon2Color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(
            horizontal = 8.dp
        )
    )
}

private fun createTestTransactions(): Map<Instant, List<Transaction>> {
    val today = Instant.now()
    val yesterday = today.minusSeconds(24 * 60 * 60) // Вчора

    val transactions = listOf(
        Transaction(
            id = 0,
            userId = 0L,
            title = "Salary",
            amount = 1000f,
            transactionType = TransactionType.INCOME,
            repeatType = RepeatType.NOT_REPEAT,
            note = "Enjoyed a coffee and a snack at Starbucks with Rick and M.",
            transactionDate = today.toEpochMilli()
        ),
        Transaction(
            id = 1,
            userId = 0L,
            title = "Food",
            amount = 50f,
            transactionType = TransactionType.FOOD,
            repeatType = RepeatType.NOT_REPEAT,
            note = "Enjoyed a coffee and a snack at Starbucks with Rick and M.",
            transactionDate = today.toEpochMilli()
        ),
        Transaction(
            id = 2,
            userId = 0L,
            title = "Transport",
            amount = 30f,
            transactionType = TransactionType.TRANSPORTATION,
            repeatType = RepeatType.NOT_REPEAT,
            transactionDate = yesterday.toEpochMilli()
        ),
        Transaction(
            id = 3,
            userId = 0L,
            title = "Entertainment",
            amount = 20f,
            transactionType = TransactionType.ENTERTAINMENT,
            repeatType = RepeatType.NOT_REPEAT,
            transactionDate = yesterday.toEpochMilli()
        ),
        Transaction(
            id = 4,
            userId = 0L,
            title = "Clothing",
            amount = 80f,
            transactionType = TransactionType.CLOTHING,
            repeatType = RepeatType.NOT_REPEAT,
            transactionDate = yesterday.toEpochMilli()
        )
    )

    return transactions.groupBy { Instant.ofEpochMilli(it.transactionDate) }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    SpendLessAppTheme {
        DashboardScreen(
            state = DashboardState(
                latestTransactions = createTestTransactions(),
            ),
            onAction = {}
        )
    }
}