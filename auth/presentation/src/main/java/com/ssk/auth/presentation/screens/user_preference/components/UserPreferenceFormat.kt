package com.ssk.auth.presentation.screens.user_preference.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun UserPreferenceFormat(
    modifier: Modifier = Modifier,
    formattedValue: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        ,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = formattedValue,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "spend this month",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPreferenceFormatPreview() {
    SpendLessAppTheme {
        UserPreferenceFormat(
            formattedValue = "₹ 1,00,000"
        )
    }
}