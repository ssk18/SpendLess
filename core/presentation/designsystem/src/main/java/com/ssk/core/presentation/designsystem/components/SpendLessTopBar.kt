@file:OptIn(ExperimentalMaterial3Api::class)

package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.ArrowBack
import com.ssk.core.presentation.designsystem.theme.DownloadIcon
import com.ssk.core.presentation.designsystem.theme.ExitIcon
import com.ssk.core.presentation.designsystem.theme.SettingsIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.onPrimaryFixed

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onPrimary,
    startIcon: ImageVector? = ArrowBack,
    onStartIconClick: (() -> Unit)? = null,
    endIcon1: ImageVector? = null,
    endIcon1Color: Color = MaterialTheme.colorScheme.error,
    endIcon1BackgroundColor: Color = MaterialTheme.colorScheme.error,
    onEndIcon1Click: (() -> Unit)? = null,
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
            .defaultMinSize(minHeight = 64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        startIcon?.let {
            Icon(
                imageVector = startIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onStartIconClick?.invoke()
                    }
            )
        }

        title?.let {
            if (startIcon == null) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                color = titleColor,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        endIcon1?.let { icon ->
            IconChip(
                icon = icon,
                iconColor = endIcon1Color,
                backgroundColor = endIcon1BackgroundColor,
                onClick = onEndIcon1Click
            )
        }

        endIcon2?.let { icon ->
            IconChip(
                icon = icon,
                iconColor = endIcon2Color,
                backgroundColor = endIcon2BackgroundColor,
                onClick = onEndIcon2Click
            )
        }
    }
}

@Composable
fun IconChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .padding(end = 8.dp)
            .size(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .clickable { onClick?.invoke() }
        )
    }
}

@Preview(name = "All TopBar Scenarios", showBackground = true)
@Composable
fun PreviewAllTopBarScenarios() {
    SpendLessAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                // Scenario #1: No start icon, two end icons, with title
                Surface(color = onPrimaryFixed) {
                    AppTopBar(
                        startIcon = null,
                        endIcon1 = DownloadIcon,
                        endIcon2 = SettingsIcon,
                        endIcon1Color = MaterialTheme.colorScheme.onPrimary,
                        endIcon1BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                        endIcon2Color = MaterialTheme.colorScheme.onPrimary,
                        endIcon2BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                        title = "Two End Icons",
                    )
                }

                HorizontalDivider()

                // Scenario #2: No start icon, single end icon, with title
                AppTopBar(
                    startIcon = null,
                    endIcon1 = ExitIcon,
                    endIcon1Color = MaterialTheme.colorScheme.error,
                    endIcon1BackgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                    // No second icon
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    title = "One End Icon",
                )

                HorizontalDivider()

                // Scenario 3: With start icon, both end icons, with title
                Surface(color = onPrimaryFixed) {
                    AppTopBar(
                        startIcon = ArrowBack,
                        endIcon1 = DownloadIcon,
                        endIcon2 = ExitIcon,
                        endIcon1Color = MaterialTheme.colorScheme.onPrimary,
                        endIcon1BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                        endIcon2Color = MaterialTheme.colorScheme.error,
                        endIcon2BackgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                        title = "Full House",
                    )
                }

                HorizontalDivider()

                // Scenario #4: With start icon, no end icons, with title
                AppTopBar(
                    startIcon = ArrowBack,
                    // No end icons
                    titleColor = MaterialTheme.colorScheme.primary,
                    title = "Start Icon Only",
                )

                HorizontalDivider()

                // Scenario #5: With title only (no icons at all)
                Surface(color = onPrimaryFixed) {
                    AppTopBar(
                        startIcon = null,
                        endIcon1 = null,
                        endIcon2 = null,
                        title = "Title Only"
                    )
                }
            }
        }
    }
}