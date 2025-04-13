package com.ssk.dashboard.presentation.dashboard.create_transaction.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun ReceiverTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    isExpense: Boolean,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        modifier = modifier
            .wrapContentWidth()
            .onFocusChanged {
                isFocused = it.isFocused
            },
        state = state,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = { innerBox ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                innerBox()
                if (state.text.isEmpty() && !isFocused) {
                    ReceiverPlaceHolder(
                        isExpense = isExpense
                    )
                }
            }
        },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction
    )
}

@Composable
fun ReceiverPlaceHolder(
    modifier: Modifier = Modifier,
    isExpense: Boolean
) {
    Text(
        text = if (isExpense) {
            "Receiver"
        } else {
            "Sender"
        },
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.outline
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ReceiverTextFieldPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReceiverTextField(
                state = TextFieldState(),
                isExpense = true
            )
        }
    }
}
