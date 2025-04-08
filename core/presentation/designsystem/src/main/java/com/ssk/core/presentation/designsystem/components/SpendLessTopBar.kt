package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.ArrowBack

@Composable
fun SpendLessTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onPrimary,
    leadingIcon: ImageVector? = ArrowBack,
    onLeadingIconClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    trailingIconColor: Color = MaterialTheme.colorScheme.error,
    trailingIconBackgroundColor: Color = MaterialTheme.colorScheme.error,
    endIcon2: ImageVector? = null,
    endIcon2Color: Color = MaterialTheme.colorScheme.error,
    endIcon2BackgroundColor: Color = MaterialTheme.colorScheme.error,
    onEndIcon2Click: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(Color.Transparent)
            .fillMaxWidth()
            .defaultMinSize(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onLeadingIconClick?.invoke() }
            )
        }

        title?.let {
            if (leadingIcon == null) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = it,
                color = titleColor,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }
}