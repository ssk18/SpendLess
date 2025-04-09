package com.ssk.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ssk.core.presentation.designsystem.theme.primaryFixed
import com.ssk.core.presentation.designsystem.theme.secondaryFixed

object AppColorStyles {
    @Composable
    fun IconBackground(isIncome: Boolean): Color {
       return if (isIncome) {
           secondaryFixed.copy(alpha = 0.4f)
        } else {
           primaryFixed
       }
    }
}