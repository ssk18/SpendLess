package com.ssk.auth.presentation.pinentryscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.SpendLessContainer
import com.ssk.core.presentation.designsystem.theme.SpendLessPurple

@Composable
fun PinEntryItem(
    modifier: Modifier = Modifier,
    pin: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(108.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SpendLessContainer)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pin,
            style = MaterialTheme.typography.titleMedium,
            color = SpendLessPurple,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(108.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SpendLessContainer.copy(0.3f))
            .clickable {
                onDeleteClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun PinEntry(
    modifier: Modifier = Modifier,
    onPinClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearPin: () -> Unit
) {
    val pins = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(pins) { pin ->
            PinEntryItem(
                pin = pin,
                onClick = {
                    onPinClick(pin)
                }
            )
        }
        item {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
            )
        }
        item {
            PinEntryItem(
                pin = "0",
                onClick = {
                    onPinClick("0")
                }
            )
        }
        item {
            DeleteButton(
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PinEntryItemPreview() {
    SpendLessAppTheme {
        PinEntry(
            onPinClick = {},
            onDeleteClick = {},
            onClearPin = {}
        )
    }
}
