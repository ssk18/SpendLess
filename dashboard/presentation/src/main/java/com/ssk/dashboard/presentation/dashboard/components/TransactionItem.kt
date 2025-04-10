package com.ssk.dashboard.presentation.dashboard.components

import SpendLessSuccess
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.math.BigDecimal

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    icon: String,
    incomeIcon: String = "\uD83D\uDCB0",
    title: String,
    category: String,
    description: String? = null,
    amount: BigDecimal,
    incomeAmountColor: Color = SpendLessSuccess,
    expenseAmountColor: Color = MaterialTheme.colorScheme.onSurface,
    isCollapsible: Boolean = true,

) {

}