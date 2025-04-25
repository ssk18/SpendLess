package com.ssk.auth.presentation.screens.pinentryscreen.components

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
import com.ssk.core.presentation.designsystem.theme.onPrimaryFixed
import com.ssk.core.presentation.designsystem.theme.primaryFixed

@Composable
fun PinEntryItem(
    modifier: Modifier = Modifier,
    pin: String,
    isLocked: Boolean,
    alpha: Float = 1f,
    onClick: (() -> Unit)
) {
    val adjustedAlpha = if (isLocked) alpha * 0.3f else alpha
    Box(
        modifier = modifier
            .size(108.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(primaryFixed.copy(adjustedAlpha))
            .clickable(
                enabled = !isLocked
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pin,
            style = MaterialTheme.typography.titleMedium,
            color = onPrimaryFixed.copy(adjustedAlpha),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    isLocked: Boolean,
    alpha: Float = 1f,
    onDeleteClick: () -> Unit
) {
    val adjustedAlpha = if (isLocked) alpha * 0.3f else alpha
    Box(
        modifier = modifier
            .size(108.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(primaryFixed.copy(0.3f))
            .clickable(
                enabled = !isLocked
            ) {
                onDeleteClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onBackground.copy(adjustedAlpha),
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun PinEntry(
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    onPinClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
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
                },
                isLocked = isLocked
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
                },
                isLocked = isLocked
            )
        }
        item {
            DeleteButton(
                onDeleteClick = onDeleteClick,
                isLocked = isLocked
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
        )
    }
}
