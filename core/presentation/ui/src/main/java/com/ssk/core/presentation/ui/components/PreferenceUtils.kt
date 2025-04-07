package com.ssk.core.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.components.SegmentOption
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.R

enum class ExpensesFormat(override val label: @Composable (() -> Unit)) : SegmentOption {
    MIUNS(
        label = {
            OptionText(text = stringResource(R.string._10))
        }
    ),
    BRACKETS(
        label = {
            OptionText(text = stringResource(R.string.parentheses_10))
        }
    ),
}

enum class DecimalSeparator(
    override val label: @Composable (() -> Unit),
    val separator: String
) : SegmentOption {
    DOT(
        label = {
            OptionText(text = "1.00")
        },
        separator = "."
    ),
    COMMA(
        label = {
            OptionText(text = "1,00")
        },
        separator = ","
    ),
}

enum class ThousandsSeparator(
    override val label: @Composable (() -> Unit),
    val separator: String
) : SegmentOption {
    DOT(
        label = {
            OptionText(text = "1.000")
        },
        separator = "."
    ),
    COMMA(
        label = {
            OptionText(text = "1,000")
        },
        separator = ","
    ),
    SPACE(
        label = {
            OptionText(text = "1 000")
        },
        separator = " "
    ),
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpendLessSegmentedSelectorPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SpendLessSegmentSelector(
                segmentOptions = ThousandsSeparator.entries,
                selectedOption = ThousandsSeparator.DOT,
                onOptionSelected = {},
            )
            SpendLessSegmentSelector(
                segmentOptions = DecimalSeparator.entries,
                selectedOption = DecimalSeparator.DOT,
                onOptionSelected = {},
            )
        }
    }
}