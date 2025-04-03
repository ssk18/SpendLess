package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SpendLessActionButton(
    modifier: Modifier = Modifier,
    text: String,
    enabledTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    disabledTextStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    enabledContainerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.onSurface.copy(
        alpha = 0.12f
    ),
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(
        alpha = 0.38f
    ),
    enabled: Boolean = true,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledContainerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        shape = RoundedCornerShape(100.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        ButtonContent(
            text = text,
            isEnabled = enabled,
            enabledTextStyle = enabledTextStyle,
            disabledTextStyle = disabledTextStyle,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            icon = icon
        )
    }
}

@Composable
fun RowScope.ButtonContent(
    text: String,
    isEnabled: Boolean,
    enabledTextStyle: TextStyle,
    disabledTextStyle: TextStyle,
    contentColor: Color,
    disabledContentColor: Color,
    icon: ImageVector?,
    iconDescription: String? = null,
) {
    Text(
        text = text,
        style = if (isEnabled) enabledTextStyle else disabledTextStyle,
        color = if (isEnabled) contentColor else disabledContentColor,
    )

    icon?.let { imageVector ->
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(top = 2.dp),
            imageVector = imageVector,
            contentDescription = iconDescription
        )
    }
}