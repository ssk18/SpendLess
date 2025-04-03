package com.ssk.auth.presentation.components

import SpendLessWhite
import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun ThousandsSeparatorExample(
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf(ThousandsSeparator.POINT) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Thousands separator",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ThousandsSeparator(
            segmentedOptions = ThousandsSeparator.values().toList(),
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ThousandsSeparator(
    modifier: Modifier = Modifier,
    segmentedOptions: List<ThousandsSeparator>,
    selectedOption: ThousandsSeparator = ThousandsSeparator.POINT,
    onOptionSelected: (ThousandsSeparator) -> Unit
) {
    val selectedIndex = segmentedOptions.indexOf(selectedOption)
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        val totalWidth = constraints.maxWidth
        val segmentWidth = totalWidth / segmentedOptions.size

        val targetPosition = selectedIndex * segmentWidth

        val animatedPosition by animateIntAsState(
            targetValue = targetPosition,
            animationSpec = tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing
            )
        )


        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            segmentedOptions.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    option.label()
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(4.dp)
                .expandWidth(totalWidth, segmentedOptions.size)
                .fillMaxHeight()
                .offset { IntOffset(x = animatedPosition, y = 0) }
                .clip(RoundedCornerShape(12.dp))
                .background(SpendLessWhite)
                .zIndex(-1f)
        )
    }
}

@Composable
fun Modifier.expandWidth(totalWidth: Int, itemCount: Int, padding: Dp = 8.dp): Modifier =
    this.then(
        object : LayoutModifier {
            override fun MeasureScope.measure(
                measurable: Measurable,
                constraints: Constraints
            ): MeasureResult {
                val segmentWidth = totalWidth / itemCount
                val paddingPx = padding.roundToPx()
                val adjustedWidth = segmentWidth - paddingPx
                val newConstraints = Constraints.fixed(
                    width = adjustedWidth,
                    height = constraints.maxHeight
                )

                val placeable = measurable.measure(newConstraints)
                return layout(adjustedWidth, placeable.height) {
                    placeable.place(0, 0)
                }
            }
        }
    )

enum class ThousandsSeparator(val label: @Composable () -> Unit, val separator: String) {
    POINT(label = { OptionText(text = "1.000") }, separator = "."),
    COMMA(label = { OptionText(text = "1,000") }, separator = ","),
    SPACE(label = { OptionText(text = "1 000") }, separator = " ")
}

@Composable
fun OptionText(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ThousandsSeparatorPreview() {
    SpendLessAppTheme {
        ThousandsSeparatorExample()
    }
}