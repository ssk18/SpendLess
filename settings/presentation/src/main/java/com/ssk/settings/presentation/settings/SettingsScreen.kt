package com.ssk.settings.presentation.settings

import SpendLessWhite
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.components.appShadow
import com.ssk.core.presentation.designsystem.theme.ArrowBack
import com.ssk.core.presentation.designsystem.theme.ExitIcon
import com.ssk.core.presentation.designsystem.theme.LockIcon
import com.ssk.core.presentation.designsystem.theme.SettingsIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "Settings",
                startIcon = ArrowBack,
                onStartIconClick = onBackClick,
                titleColor = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val shadowModifier = Modifier.appShadow(
                blurRadius = 24.dp,
                positionY = 8.dp
            )

            SettingsCard(
                modifier = shadowModifier
            ) {
                Column {
                    SettingsButton(
                        icon = SettingsIcon,
                        title = "Preferences",
                        onClick = { /* TODO: Handle Preferences Click */ },
                    )
                    SettingsButton(
                        icon = LockIcon,
                        title = "Security",
                        onClick = { /* TODO: Handle Security Click */ },
                    )
                }
            }

            SettingsCard(
                modifier = shadowModifier
            ) {
                SettingsButton(
                    icon = ExitIcon,
                    title = "Log out",
                    background = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.08f),
                    iconColor = MaterialTheme.colorScheme.error,
                    titleColor = MaterialTheme.colorScheme.error,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    background: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp)),
                color = background,
                contentColor = iconColor,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(color = titleColor)
            )
        }
    }
}

@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = SpendLessWhite,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = colors,
        shape = shape,
        elevation = elevation,
    ) {
        content()
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SpendLessAppTheme {
        SettingsScreen(
            onBackClick = {}
        )
    }
}