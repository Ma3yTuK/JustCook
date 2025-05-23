package com.example.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe
import com.example.data.models.short_models.RecipeShort

@Composable
fun RecipeStars(
    recipeRating: Float,
    recipeIsPremium: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        for (star in 1..recipeRating.toInt()) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if(recipeIsPremium) JustCookColorPalette.colors.iconGold else JustCookColorPalette.colors.iconPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}