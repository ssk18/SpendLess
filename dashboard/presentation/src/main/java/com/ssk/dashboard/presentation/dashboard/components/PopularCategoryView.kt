package com.ssk.dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.core.presentation.designsystem.components.SpendLessGradientBackground
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.designsystem.theme.primaryFixed

@Composable
fun PopularCategoryView(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    description: String,
    iconBackground: Color = primaryFixed,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        CategoryIcon(
            icon = icon,
            iconBackground = iconBackground,
        )
        Spacer(modifier = Modifier.width(8.dp))
        CategoryInfo(
            title = title,
            description = description
        )
    }
}

@Composable
fun CategoryIcon(
    modifier: Modifier = Modifier,
    icon: String,
    iconBackground: Color,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = iconBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 30.sp
        )
    }
}

@Composable
fun RowScope.CategoryInfo(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
) {
    Column(
        modifier = modifier
            .align(Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        )
    }
}

@Preview
@Composable
fun PopularCategoryViewPreview() {
    SpendLessAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SpendLessGradientBackground {
                PopularCategoryView(
                    modifier = Modifier.padding(16.dp),
                    icon = "🍕",
                    title = "Food & Groceries",
                    description = "Most popular category"
                )
            }
        }
    }
}
