package com.ssk.spendless.core.presentation.designsystem.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun SpendLessTextField(
    modifier: Modifier = Modifier,
    userName: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = userName,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        shape = RoundedCornerShape(5.dp),
        modifier = modifier
            .minimumInteractiveComponentSize(),
    )
}

@Preview(showBackground = true)
@Composable
fun SpendLessTextFieldPreview() {
    SpendLessAppTheme {
        SpendLessTextField(
            userName = "",
            onValueChange = {},
        )
    }
}