package com.example.catalogue.recipe_detail.components.body.components.description.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.components.LocalSharedTransitionScope
import com.example.components.theme.JustCookColorPalette

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TextButton(
    buttonText: String,
    onButtonClick: () -> Unit
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current ?: throw IllegalStateException("No scope found")
    with(sharedTransitionScope) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            color = JustCookColorPalette.colors.textLink,
            modifier = Modifier
                .heightIn(20.dp)
                .fillMaxWidth()
                .padding(top = 15.dp)
                .clickable {
                    onButtonClick()
                }
                .skipToLookaheadSize()
        )
    }
}