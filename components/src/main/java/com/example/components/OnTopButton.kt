package com.example.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import com.example.components.theme.Neutral8

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.OnTopButton(
    onPress: () -> Unit,
    painter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")
    with(animatedVisibilityScope) {
        IconButton(
            onClick = onPress,
            modifier = modifier
                .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 3f)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .size(36.dp)
                .animateEnterExit(
                    enter = scaleIn(tween(300, delayMillis = 300)),
                    exit = scaleOut(tween(20))
                )
                .background(
                    color = Neutral8.copy(alpha = 0.32f),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painter,
                tint = JustCookColorPalette.colors.iconInteractive,
                contentDescription = contentDescription
            )
        }
    }
}