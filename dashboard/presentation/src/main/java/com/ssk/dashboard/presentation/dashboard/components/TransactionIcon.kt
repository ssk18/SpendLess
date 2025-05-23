package com.ssk.dashboard.presentation.dashboard.components

import SpendLessWhite
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.presentation.designsystem.theme.NoteIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.primaryFixed
import com.ssk.core.presentation.designsystem.theme.secondaryFixed
import com.ssk.core.presentation.designsystem.theme.secondaryFixedDim

@Composable
fun TransactionIcon(
    modifier: Modifier = Modifier,
    transaction: Transaction
) {
    val transactionType = transaction.transactionType
    val backgroundColor = when (transactionType) {
        TransactionType.INCOME -> secondaryFixed.copy(alpha = 0.4f)
        else -> primaryFixed
    }

    val noteIconTint = when (transactionType) {
        TransactionType.INCOME -> secondaryFixedDim
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                shape = RoundedCornerShape(12.dp),
                color = backgroundColor
            ),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = transaction.transactionType.symbol,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        // Only show note icon if note is not null and not empty
        if (transaction.note != null && transaction.note!!.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        shape = RoundedCornerShape(6.dp),
                        color = SpendLessWhite
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = NoteIcon,
                    contentDescription = null,
                    tint = noteIconTint,
                    modifier = Modifier
                        .size(14.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionIconPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            TransactionIcon(
                transaction = Transaction(
                    transactionType = TransactionType.ENTERTAINMENT,
                    note = "Note",
                    amount = 100.0f,
                    title = "Movie",
                    userId = 1L,
                    repeatType = RepeatType.NOT_REPEAT
                )
            )
        }
    }
}
