package com.ssk.core.presentation.designsystem.theme

import SpendLessBlack
import SpendLessDarkGrey
import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.sp
import com.ssk.core.presentation.designsystem.R

@OptIn(ExperimentalTextApi::class)
val FigtreeRegular = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.figtree_variablefont_weight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val FigtreeMedium = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.figtree_variablefont_weight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(500)
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val FigtreeSemiBold = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.figtree_variablefont_weight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600)
        )
    )
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        color = SpendLessBlack
    ),

    headlineLarge = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),

    titleLarge = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FigtreeSemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FigtreeMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = SpendLessBlack
    ),

    labelMedium = TextStyle(
        fontFamily = FigtreeMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = SpendLessBlack
    ),
    labelSmall = TextStyle(
        fontFamily = FigtreeMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = FigtreeRegular,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = SpendLessDarkGrey
    ),
    bodySmall = TextStyle(
        fontFamily = FigtreeRegular,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = SpendLessDarkGrey
    )
)
