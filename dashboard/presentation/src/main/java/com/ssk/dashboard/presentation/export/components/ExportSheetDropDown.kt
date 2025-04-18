package com.ssk.dashboard.presentation.export.components

import SpendLessWhite
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.components.appShadow
import com.ssk.core.presentation.designsystem.theme.CheckIcon
import com.ssk.core.presentation.designsystem.theme.CollapsedIcon
import com.ssk.core.presentation.designsystem.theme.ExpandedIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.dashboard.presentation.export.ExportRange

@Composable
fun ExportSheetDropDown(
    modifier: Modifier = Modifier,
    selectedItem: ExportRange,
    onItemSelected: (ExportRange) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var categoryWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Export Range",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        ExportRangeCard(
            isExpanded = expanded,
            exportRange = selectedItem,
            onExportRangeClick = { expanded = !expanded },
            modifier = Modifier
                .onSizeChanged {
                    categoryWidth = with(density) { it.width.toDp() }
                }
        )

        Spacer(modifier = Modifier.height(4.dp))

        ExportDropDown(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded },
            items = ExportRange.entries,
            selectedItem = selectedItem,
            onItemSelected = {
                onItemSelected(it)
                expanded = !expanded
            },
            dropdownWidth = categoryWidth
        )

    }
}

@Composable
fun ExportRangeCard(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    exportRange: ExportRange,
    onExportRangeClick: (ExportRange) -> Unit
) {
    Card(
        modifier = modifier
            .appShadow()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onExportRangeClick(exportRange) },
        colors = CardDefaults.cardColors(
            containerColor = SpendLessWhite
        )
    ) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(exportRange.titleRes),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = if (isExpanded) ExpandedIcon else CollapsedIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}


@Composable
fun ExportDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    dropdownWidth: Dp,
    onDismissRequest: () -> Unit,
    items: List<ExportRange>,
    selectedItem: ExportRange,
    onItemSelected: (ExportRange) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(16.dp),
        containerColor = SpendLessWhite,
        shadowElevation = 8.dp,
        modifier = modifier.width(dropdownWidth)
    ) {
        items.forEach { exportRange ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(exportRange.titleRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                onClick = { onItemSelected(exportRange) },
                trailingIcon = {
                    if (exportRange == selectedItem) {
                        Icon(
                            imageVector = CheckIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(end = 12.dp)
                        )
                    }
                },
                contentPadding = PaddingValues(horizontal = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExportRangeCardPreview() {
    SpendLessAppTheme {
        ExportRangeCard(
            isExpanded = false,
            exportRange = ExportRange.LAST_MONTH,
            onExportRangeClick = {}
        )
    }
}