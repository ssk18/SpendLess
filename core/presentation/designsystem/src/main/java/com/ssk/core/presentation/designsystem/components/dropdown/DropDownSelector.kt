@file:OptIn(ExperimentalMaterial3Api::class)

package com.ssk.core.presentation.designsystem.components.dropdown

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.core.domain.model.RepeatType
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.TickIcon
import com.ssk.core.presentation.designsystem.theme.primaryFixed

@Composable
fun <T> DropDownSelector(
    modifier: Modifier = Modifier,
    title: String? = null,
    options: List<T>,
    currencyCodeShow: (T) -> String,
    currencyNameShow: (T) -> String,
    selectedOption: T,
    showIcnBackground: Boolean = false,
    iconBackgroundColor: Color = primaryFixed,
    fontStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onOptionSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    var dropDownHeight by remember {
        mutableStateOf(0.dp)
    }

    val animateDropDownHeight by animateDpAsState(
        targetValue = if (expanded) {
            dropDownHeight
        } else {
            0.dp
        },
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing,
        ),
        label = "DropDownHeight"
    )

    val animateContentAlpha by animateFloatAsState(
        targetValue = if (expanded) {
            1f
        } else {
            0f
        },
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing,
        ),
        label = "ContentAlpha"
    )

    Column(
        modifier = modifier
    ) {
        title?.let {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            },
        ) {
            CurrencyTextField(
                expanded = expanded,
                fontStyle = fontStyle,
                currencyCodeShow = currencyCodeShow,
                currencyNameShow = currencyNameShow,
                onExpandedChange = {
                    expanded = it
                },
                selectedOption = selectedOption,
                showIconBackground = showIcnBackground,
                iconBackgroundColor = iconBackgroundColor,
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                    .onGloballyPositioned { coordinates ->
                        with(density) {
                            dropDownHeight = coordinates.size.height.toDp()
                        }
                    }
                    .graphicsLayer {
                        translationY = if (expanded) 0f else -animateDropDownHeight.toPx()
                        alpha = animateContentAlpha
                        clip = true
                        shape = RoundedCornerShape(16.dp)
                    }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.height(48.dp),
                        text = {
                            CurrencyRow(
                                fontStyle = fontStyle,
                                currencyCode = currencyCodeShow(option),
                                currencyName = currencyNameShow(option),
                                showIconBackground = showIcnBackground,
                                iconBackgroundColor = iconBackgroundColor,
                                showTickIcon = option == selectedOption
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        contentPadding = PaddingValues(
                            vertical = 0.dp,
                            horizontal = 0.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun <T> ExposedDropdownMenuBoxScope.CurrencyTextField(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    selectedOption: T,
    showIconBackground: Boolean,
    iconBackgroundColor: Color,
    currencyCodeShow: (T) -> String,
    currencyNameShow: (T) -> String,
    fontStyle: TextStyle,
    onExpandedChange: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = "",
        onValueChange = {},
        readOnly = true,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable {
                onExpandedChange(!expanded)
            },
        decorationBox = @Composable {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyRow(
                    modifier = Modifier
                        .weight(1f),
                    currencyCode = currencyCodeShow(selectedOption),
                    currencyName = currencyNameShow(selectedOption),
                    fontStyle = fontStyle,
                    showIconBackground = showIconBackground,
                    iconBackgroundColor = iconBackgroundColor,
                )
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    )
}

@Composable
private fun CurrencyRow(
    modifier: Modifier = Modifier,
    currencyCode: String,
    currencyName: String,
    fontStyle: TextStyle,
    showIconBackground: Boolean,
    iconBackgroundColor: Color,
    showTickIcon: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showIconBackground) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .background(
                        color = iconBackgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currencyCode,
                    fontSize = 20.sp,
                )
            }
        } else {
            Text(
                text = currencyCode,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = currencyName,
            style = fontStyle,
            textAlign = TextAlign.End
        )
        if (showTickIcon) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = TickIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
    }
}

enum class FakeCurrency(val symbol: String, val title: String) {
    INR("₹", "Indian Rupee"),
    USD("$", "US Dollar"),
    EUR("€", "Euro")
}


@Preview(showBackground = true)
@Composable
fun DropDownSelectorPreview() {
    SpendLessAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                DropDownSelector(
                    title = "Currency",
                    options = FakeCurrency.entries,
                    onOptionSelected = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    currencyCodeShow = { it.symbol },
                    currencyNameShow = { it.title },
                    selectedOption = FakeCurrency.INR,
                    fontStyle = MaterialTheme.typography.bodyMedium
                )
                DropDownSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    options = TransactionType.entries,
                    onOptionSelected = {},
                    currencyCodeShow = { it.symbol },
                    currencyNameShow = { it.type },
                    selectedOption = TransactionType.FOOD,
                    showIcnBackground = true,
                )
                DropDownSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    options = RepeatType.entries,
                    onOptionSelected = {},
                    currencyCodeShow = { it.symbol },
                    currencyNameShow = { it.title },
                    selectedOption = RepeatType.NOT_REPEAT,
                    showIcnBackground = true,
                )
            }
        }
    }
}