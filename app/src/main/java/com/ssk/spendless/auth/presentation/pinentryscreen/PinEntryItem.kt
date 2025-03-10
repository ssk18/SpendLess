package com.ssk.spendless.auth.presentation.pinentryscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun PinEntryItem(
    modifier: Modifier = Modifier,
    pin: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(108.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pin.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PinEntry(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
    ) {
       items(9) {
           PinEntryItem(
               pin = it + 1,
               onClick = onClick
           )
       }
    }
}

@Preview(showBackground = true)
@Composable
fun PinEntryItemPreview() {
    SpendLessAppTheme {
        PinEntry(
            onClick = {}
        )
    }
}
