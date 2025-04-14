package com.ssk.dashboard.presentation.create_transaction.components

import SpendLessSuccess
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.domain.model.ExpensesFormat
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.dashboard.presentation.R

@Composable
fun AmountTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    isExpense: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    expensesFormat: ExpensesFormat
) {
    val prefixSign = when (expensesFormat) {
        ExpensesFormat.MINUS -> "-"
        ExpensesFormat.BRACKETS -> "("
    }

    val prefix = if (isExpense) {
        stringResource(R.string.expense_sign, prefixSign)
    } else stringResource(R.string.income_sign)

    val signsColor = if (isExpense) {
        MaterialTheme.colorScheme.error
    } else SpendLessSuccess

    BasicTextField(
        modifier = modifier
            .wrapContentWidth(),
        state = state,
        textStyle = MaterialTheme.typography.displayMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = { innerBox ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrefixSign(
                        sign = prefix,
                        color = signsColor
                    )

                    Box(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                    ) {
                        AmountPlaceHolder(amount = state.text.toString())
                        innerBox()
                    }

                    if (isExpense) {
                        SuffixSign(
                            expensesFormat = expensesFormat,
                            color = signsColor
                        )
                    }
                }
            }
        },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction
    )
}

@Composable
fun AmountPlaceHolder(
    modifier: Modifier = Modifier,
    amount: String
) {
    Text(
        text = "00.00",
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        color = if (amount.isEmpty()) {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        } else {
            Color.Transparent
        }
    )
}

@Composable
fun PrefixSign(
    modifier: Modifier = Modifier,
    sign: String,
    color: Color
) {
    Text(
        text = sign,
        modifier = modifier.padding(end = 4.dp),
        style = MaterialTheme.typography.displayMedium,
        color = color
    )
}

@Composable
fun SuffixSign(
    modifier: Modifier = Modifier,
    expensesFormat: ExpensesFormat,
    color: Color
) {
    Text(
        text = when (expensesFormat) {
            ExpensesFormat.MINUS -> ""
            ExpensesFormat.BRACKETS -> ")"
        },
        modifier = modifier.padding(start = 4.dp),
        style = MaterialTheme.typography.displayMedium,
        color = color
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AmountTextFieldPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmountTextField(
                state = TextFieldState(),
                isExpense = true,
                expensesFormat = ExpensesFormat.MINUS
            )
        }
    }
}