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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
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
fun CardCalories(
    recipe: Recipe,
    collectionId: Long
) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    with(sharedTransitionScope) {
        Text(
            text = "${recipe.calories} ${stringResource(R.string.calories)}",
            style = MaterialTheme.typography.bodyLarge,
            color = JustCookColorPalette.colors.textHelp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .sharedBounds(
                    rememberSharedContentState(
                        key = RecipeSharedElementKey(
                            recipeId = recipe.id,
                            type = RecipeSharedElementType.Calories,
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