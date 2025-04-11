package com.ssk.dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.core.presentation.designsystem.components.SpendLessGradientBackground
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.primaryFixed
import com.ssk.core.presentation.designsystem.theme.secondaryFixed
import com.ssk.dashboard.presentation.R
import com.ssk.dashboard.presentation.dashboard.DashboardState

@Composable
fun DashBoardContent(
    modifier: Modifier = Modifier,
    state: DashboardState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = state.accountInfoState.accountBalance,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        )
        Text(
            text = "Total Balance",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(26.dp))

        state.accountInfoState.popularCategory?.let {
            PopularCategoryView(
                icon = it.symbol,
                title = it.title,
                description = "Most popular category"
            )
        }

    }
}

@Composable
fun PopularCategoryView(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    description: String,
    iconBackground: Color = primaryFixed,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        CategoryIcon(
            icon = icon,
            iconBackground = iconBackground,
        )
        Spacer(modifier = Modifier.width(8.dp))
        CategoryInfo(
            title = title,
            description = description
        )
    }
}

@Composable
fun CategoryIcon(
    modifier: Modifier = Modifier,
    icon: String,
    iconBackground: Color,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = iconBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 30.sp
        )
    }
}

@Composable
fun RowScope.CategoryInfo(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
) {
    Column(
        modifier = modifier
            .align(Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun LargestTransaction(
    modifier: Modifier = Modifier,
    largestTransaction: DashboardState.LargestTransaction?
) {
    Surface(
        modifier = modifier
            .height(72.dp),
        shape = RoundedCornerShape(16.dp),
        color = primaryFixed,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (largestTransaction != null) {
                LargestTransactionInfo(
                    largestTransaction = largestTransaction,
                )
            } else {
                Text(
                    text = "Your largest transaction will appear here",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LargestTransactionInfo(
    modifier: Modifier = Modifier,
    largestTransaction: DashboardState.LargestTransaction
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = largestTransaction.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Largest transaction",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = largestTransaction.amount,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = largestTransaction.date,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun PreviousWeekExpense(
    modifier: Modifier = Modifier,
    amount: String,
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(132.dp),
        shape = RoundedCornerShape(16.dp),
        contentColor = MaterialTheme.colorScheme.onSurface,
        color = secondaryFixed
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 2.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.previous_week),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LargestTransactionPreview() {
    SpendLessAppTheme {
        SpendLessGradientBackground {
            LargestTransaction(
                largestTransaction = DashboardState.LargestTransaction(
                    name = "Transaction",
                    amount = "$1000",
                    date = "2023-01-01"
                ),
                modifier = Modifier.padding(8.dp)
            )

            LargestTransaction(
                largestTransaction = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PopularCategoryViewPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SpendLessGradientBackground {
                PopularCategoryView(
                    modifier = Modifier.padding(16.dp),
                    icon = "🍕",
                    title = "Food & Groceries",
                    description = "Most popular category"
                )
            }
        }
    }
}
