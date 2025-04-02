package com.ssk.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

val primaryBackground = Color(0xffffd8ed)

@Composable
fun ThousandsPicker(
    modifier: Modifier = Modifier,
    options: List<String> = listOf("1.000", "1,000", "1 000"),
    selectedIndex: Int = 0,
    onOptionSelected: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(primaryBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .widthIn()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) Color.White else Color.Transparent
                        )
                        .clickable { onOptionSelected(index) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color(0xFF6E3EA6) /* darker purple */
                        else Color(0xFF7B5294), /* normal purple text */
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThousandsPickerPreview() {
    SpendLessAppTheme {
        ThousandsPicker(
            onOptionSelected = {}
        )
    }
}