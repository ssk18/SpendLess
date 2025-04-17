@file:OptIn(ExperimentalFoundationApi::class)

package com.ssk.dashboard.presentation.all_transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.domain.utils.InstantFormatter
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.ArrowBack
import com.ssk.core.presentation.designsystem.theme.DownloadIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.dashboard.presentation.TransactionSharedViewModel
import com.ssk.dashboard.presentation.dashboard.components.TransactionItemView
import com.ssk.dashboard.presentation.dashboard.createTestTransactions
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllTransactionsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: TransactionSharedViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "All Transactions",
                startIcon = ArrowBack,
                onStartIconClick = {
                    onBackClick()
                },
                endIcon2 = DownloadIcon,
                onEndIcon2Click = {},
                endIcon2BackgroundColor = MaterialTheme.colorScheme.background,
                endIcon2Color = MaterialTheme.colorScheme.onBackground,
                titleColor = MaterialTheme.colorScheme.onBackground
            )
        }
    ) {
        AllTransactionsScreen(
            modifier = Modifier.padding(it),
            state = viewModel.allTransactionsUiState
        )
    }
}

@Composable
fun AllTransactionsScreen(
    modifier: Modifier = Modifier,
    state: AllTransactionsUiState,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        state.transactions.entries.forEach { (instant, transactions) ->
            stickyHeader {
                Text(
                    text = InstantFormatter.formatToRelativeDay(instant),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.70f
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.background)
                )
            }

            items(
                items = transactions,
                key = { transaction -> transaction.id }
            ) { transaction ->
                TransactionItemView(
                    transaction = transaction,
                    amountSettings = state.amountSettings
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AllTransactionsScreenPreview() {
    SpendLessAppTheme {
        AllTransactionsScreen(
            state = AllTransactionsUiState(
                transactions = createTestTransactions()
            )
        )
    }
}