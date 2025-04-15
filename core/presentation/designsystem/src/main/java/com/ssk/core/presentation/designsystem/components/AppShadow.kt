package com.ssk.core.presentation.designsystem.components

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.appShadow(
    blurRadius: Dp = 20.dp,
    cornerRadius: Dp = 16.dp,
    spread: Dp = 4.dp,
    positionX: Dp = 0.dp,
    positionY: Dp = 6.dp,
    shadowAlpha: Float = 0.1f
): Modifier {
    return  drawBehind {
        drawIntoCanvas {  canvas ->
            val shadowColor = Color(0xFF180040).copy(alpha = shadowAlpha)
            val paint = Paint().apply {
                isAntiAlias = true
                asFrameworkPaint().apply {
                    color = shadowColor.toArgb()
                    maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
                }
            }

            canvas.drawRoundRect(
                left = positionX.toPx() - spread.toPx(),
                top = positionY.toPx() - spread.toPx(),
                right = size.width + positionX.toPx() + spread.toPx(),
                bottom = size.height + positionY.toPx() + spread.toPx(),
                radiusX = cornerRadius.toPx(),
                radiusY = cornerRadius.toPx(),
                paint = paint
            )
        }
    }
}