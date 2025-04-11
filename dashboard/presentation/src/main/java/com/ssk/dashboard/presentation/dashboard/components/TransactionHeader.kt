package com.ssk.dashboard.presentation.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun TransactionHeader(
    modifier: Modifier = Modifier,
    onShowAllClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Latest Transactions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(start = 12.dp)
        )
        Text(
            modifier = Modifier
                .padding(end = 12.dp)
                .clickable {
                    onShowAllClick()
                },
            text = "Show all",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionHeaderPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TransactionHeader(
                onShowAllClick = {}
            )
        }
    }
}