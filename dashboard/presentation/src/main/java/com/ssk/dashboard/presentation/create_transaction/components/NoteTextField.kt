package com.ssk.dashboard.presentation.create_transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
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
fun NoteTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
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
                modifier = Modifier
                    .wrapContentWidth(),
                contentAlignment = Alignment.Center
            ) {
                innerBox()
                if (state.text.isEmpty() && !isFocused) {
                    NotePlaceHolder()
                }
            }
        },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction
    )
}

@Composable
fun NotePlaceHolder() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = "Add Note",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteTextFieldPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteTextField(
                state = TextFieldState(),
            )
        }
    }
}
