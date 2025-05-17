package com.example.search.search_page.components.search_bar.components.filter.components.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.components.JustSurface
import com.example.components.modifiers.offsetGradientBackground
import com.example.components.theme.JustCookColorPalette

@Composable
fun Chip(
    name: String,
    selected: Boolean,
    setSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    val backgroundColor by animateColorAsState(
        if (selected) JustCookColorPalette.colors.brandSecondary else JustCookColorPalette.colors.uiBackground,
        label = "background color"
    )
    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected,
        colors = JustCookColorPalette.colors.interactiveSecondary,
        shape = shape
    )
    val textColor by animateColorAsState(
        if (selected) Color.Black else JustCookColorPalette.colors.textSecondary,
        label = "text color"
    )

    JustSurface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        elevation = 2.dp
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed) {
                Modifier.offsetGradientBackground(
                    JustCookColorPalette.colors.interactiveSecondary,
                    200f,
                    0f
                )
            } else {
                Modifier.background(Color.Transparent)
            }
        Box(
            modifier = Modifier
                .toggleable(
                    value = selected,
                    onValueChange = setSelected,
                    interactionSource = interactionSource,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                )
            )
        }
    }
}

fun Modifier.diagonalGradientBorder(
    colors: List<Color>,
    borderSize: Dp = 2.dp,
    shape: Shape
) = border(
    width = borderSize,
    brush = Brush.linearGradient(colors),
    shape = shape
)

fun Modifier.fadeInDiagonalGradientBorder(
    showBorder: Boolean,
    colors: List<Color>,
    borderSize: Dp = 2.dp,
    shape: Shape
) = composed {
    val animatedColors = List(colors.size) { i ->
        animateColorAsState(
            if (showBorder) colors[i] else colors[i].copy(alpha = 0f),
            label = "animated color"
        ).value
    }
    diagonalGradientBorder(
        colors = animatedColors,
        borderSize = borderSize,
        shape = shape
    )
}