package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SettingsIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    enabled: Boolean = true
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(48.dp)
        )
    }
}

@Preview
@Composable
fun AppIconButtonPreview() {
    SpendLessAppTheme {
        AppIconButton(
            icon = SettingsIcon
        )
    }
}