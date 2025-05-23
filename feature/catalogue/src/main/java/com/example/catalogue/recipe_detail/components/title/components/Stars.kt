package com.example.catalogue.recipe_detail.components.title.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.RecipeStars
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Stars(
    recipe: Recipe,
    collectionIndex: Int?
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalArgumentException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")

    with(sharedTransitionScope) {
        RecipeStars(
            recipeRating = recipe.rating,
            recipeIsPremium = recipe.isPremium,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .sharedBounds(
                    rememberSharedContentState(
                        key = RecipeSharedElementKey(
                            recipeId = recipe.id,
                            type = RecipeSharedElementType.Rating,
                            collectionIndex = collectionIndex
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(nonSpatialExpressiveSpring()),
                    exit = fadeOut(nonSpatialExpressiveSpring()),
                    boundsTransform = recipeDetailBoundsTransform,
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
        )
    }
}