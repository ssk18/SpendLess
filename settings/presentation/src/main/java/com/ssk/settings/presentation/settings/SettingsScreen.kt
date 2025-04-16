package com.ssk.settings.presentation.settings

import SpendLessWhite
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
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
import com.ssk.settings.presentation.SettingsViewModel
import com.ssk.settings.presentation.settings.components.SettingsButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    navigateToPreferences: () -> Unit,
    navigateToSecurity: () -> Unit,
    navigateToLogin: () -> Unit,
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
        SettingsScreen(
            modifier = Modifier.padding(it),
            onAction = { action ->
                when (action) {
                    SettingsAction.OnLogOutClicked -> navigateToLogin()
                    SettingsAction.OnPreferencesClicked -> navigateToPreferences()
                    SettingsAction.OnSecurityClicked -> navigateToSecurity()
                }
            }
        )
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit
) {
    Column(
        modifier = modifier
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
                    onClick = {
                        onAction(SettingsAction.OnPreferencesClicked)
                    },
                )
                SettingsButton(
                    icon = LockIcon,
                    title = "Security",
                    onClick = {
                        onAction(SettingsAction.OnSecurityClicked)
                    },
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
                onClick = {
                    onAction(SettingsAction.OnLogOutClicked)
                }
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

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SpendLessAppTheme {
        SettingsScreen(
            onAction = {}
        )
    }
}