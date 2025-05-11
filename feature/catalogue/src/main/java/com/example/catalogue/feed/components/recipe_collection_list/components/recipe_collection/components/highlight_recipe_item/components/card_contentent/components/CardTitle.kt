package com.example.catalogue.feed.components.recipe_collection_list.components.recipe_collection.components.highlight_recipe_item.components.card_contentent.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.recipe_detail.RecipeSharedElementKey
import com.example.catalogue.recipe_detail.RecipeSharedElementType
import com.example.catalogue.recipe_detail.recipeDetailBoundsTransform
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardTitle(
    recipe: Recipe,
    collectionId: Long
) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    with(sharedTransitionScope) {
        Text(
            text = recipe.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge,
            color = JustCookColorPalette.colors.textSecondary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .sharedBounds(
                    rememberSharedContentState(
                        key = RecipeSharedElementKey(
                            recipeId = recipe.id,
                            type = RecipeSharedElementType.Title,
                            collectionId = collectionId
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(nonSpatialExpressiveSpring()),
                    exit = fadeOut(nonSpatialExpressiveSpring()),
                    boundsTransform = recipeDetailBoundsTransform,
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
                .wrapContentWidth()
        )
    }
}