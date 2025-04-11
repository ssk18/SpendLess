package com.ssk.dashboard.presentation.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionHeader(
    modifier: Modifier = Modifier,
    onShowAllClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Latest Transactions",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier
                .clickable {
                    onShowAllClick()
                },
            text = "Show all",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}