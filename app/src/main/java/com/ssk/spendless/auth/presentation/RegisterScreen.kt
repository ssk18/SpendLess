package com.ssk.spendless.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.spendless.R
import com.ssk.spendless.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.spendless.core.presentation.designsystem.components.SpendLessTextField
import com.ssk.spendless.core.presentation.designsystem.theme.RegisterIcon
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.spendless.core.presentation.designsystem.theme.displayFontFamily

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterState = RegisterState(),
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = RegisterIcon,
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.welcome_to_spendless),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.create_unique_username),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max) // Column width determined by its widest child
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            SpendLessTextField(
                userName = state.username,
                onValueChange = {
                    onAction(RegisterAction.OnUserNameChange)
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
            SpendLessActionButton(
                text = "Next",
                enabled = state.isButtonEnabled,
                onClick = {
                    onAction(RegisterAction.OnNextClick)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            BasicText(
                text = ""
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SpendLessAppTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}

