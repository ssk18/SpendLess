package com.ssk.dashboard.presentation.dashboard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ssk.core.domain.model.Transaction

@Composable
fun RowScope.TransactionInfo(
    modifier: Modifier = Modifier,
    transaction: Transaction,
) {
    Column(
        modifier = modifier
            .weight(1f)
    ) {
        val category = transaction.transactionType.type

        Text(
            text = transaction.title,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = category,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}