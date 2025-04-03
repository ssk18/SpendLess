package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun SpendLessTextField(
    modifier: Modifier = Modifier,
    userName: TextFieldState,
    hint: String? = null,
    keyboardType: KeyboardType,
    endIcon: ImageVector? = null,
    endIconTint: Color? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        state = userName,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
            )
            .border(
                width = 1.dp,
                color = if (isFocused) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary
                },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        decorator = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (userName.text.isEmpty() && !isFocused) {
                        hint?.let {
                            Text(
                                text = hint,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.38f
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                    innerBox()
                }
                if (endIcon != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = endIcon,
                        contentDescription = null,
                        tint = endIconTint ?: MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SpendLessTextFieldPreview() {
    SpendLessAppTheme {
        SpendLessTextField(
            userName = TextFieldState(),
            hint = "Username",
            keyboardType = KeyboardType.Email
        )
    }
}