package com.ssk.core.presentation.designsystem.components

import SpendLessPrimaryContainer
import SpendLessWhite
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.onPrimaryFixed

@Composable
fun SpendLessSegmentSelector(
    modifier: Modifier = Modifier,
    segmentOptions: List<SegmentOption>,
    selectedOption: SegmentOption,
    onOptionSelected: (SegmentOption) -> Unit
) {
    val density = LocalDensity.current
    var selectedOptionWidth by remember {
        mutableStateOf(0.dp)
    }
    var selectedOffset by remember {
        mutableStateOf(IntOffset.Zero)
    }
    var isInitialized by remember { mutableStateOf(false) }
    val targetOffsetX by animateIntAsState(
        targetValue = selectedOffset.x,
        animationSpec = if (isInitialized) tween(durationMillis = 450) else snap()
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SpendLessPrimaryContainer.copy(alpha = 0.08f))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = targetOffsetX,
                        y = 0
                    )
                }
                .clip(RoundedCornerShape(12.dp))
                .background(SpendLessWhite)
                .fillMaxHeight()
                .width(selectedOptionWidth)
        )

        Row {
            segmentOptions.forEach { option ->
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!isInitialized) {
                                isInitialized = true
                            }
                            onOptionSelected(option)
                        }
                        .onGloballyPositioned { coordinates ->
                            if (option == selectedOption) {
                                selectedOffset = IntOffset(
                                    x = coordinates.positionInParent().x.toInt(),
                                    y = coordinates.positionInParent().y.toInt()
                                )
                                selectedOptionWidth = with(density) {
                                    coordinates.size.width.toDp()
                                }
                            }
                        },
                    contentColor = if (option == selectedOption) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        onPrimaryFixed.copy(alpha = 0.7f)
                    },
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        option.label()
                    }
                }
            }
        }
    }
}

interface SegmentOption {
    val label: @Composable () -> Unit
}