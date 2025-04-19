package com.ssk.dashboard.presentation.export

sealed interface ExportUiAction {
    data object OnExportClicked : ExportUiAction
    data object OnExportSheetToggled : ExportUiAction
    data class OnExportRangeClicked(val exportRange: ExportRange) : ExportUiAction
}