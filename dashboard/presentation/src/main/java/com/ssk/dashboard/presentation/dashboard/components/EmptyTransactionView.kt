package com.ssk.dashboard.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.R
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun EmptyTransactionView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(96.dp),
            painter = painterResource(R.drawable.ic_money),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "No transactions to show",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyTransactionViewPreview() {
    SpendLessAppTheme {
        EmptyTransactionView()
    }
}