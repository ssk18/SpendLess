package com.ssk.auth.presentation.screens.user_preference.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ssk.auth.presentation.R
import com.ssk.core.presentation.designsystem.theme.ArrowBack

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = ArrowBack,
                contentDescription = stringResource(R.string.navigate_back),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}