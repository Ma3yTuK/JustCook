package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    blurRadius: Dp = 16.dp,
    overlayColor: Color = JustCookColorPalette.colors.uiBackground.copy(alpha = 0.3f),
    content: @Composable () -> Unit
) {
    content()
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier
                    .matchParentSize()
                    .blur(blurRadius)
                    .background(overlayColor)
                )
                CircularProgressIndicator()
            }
        }
    }
}