package com.example.components.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Density

fun Modifier.offsetGradientBackground(
    colors: List<Color>,
    width: Density.() -> Float,
    offset: Density.() -> Float = { 0f }
) = drawBehind {
    val actualOffset = offset()

    drawRect(
        Brush.horizontalGradient(
            colors = colors,
            startX = -actualOffset,
            endX = width() - actualOffset,
            tileMode = TileMode.Mirror
        )
    )
}

fun Modifier.offsetGradientBackground(
    colors: List<Color>,
    width: Float,
    offset: Float
) = drawBehind {
    drawRect(
        Brush.horizontalGradient(
            colors = colors,
            startX = -offset,
            endX = width - offset,
            tileMode = TileMode.Mirror
        )
    )
}