package com.example.catalogue.feed.components.recipe_collection_list.components.recipe_collection.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalogue.recipe_detail.RecipeSharedElementKey
import com.example.catalogue.recipe_detail.RecipeSharedElementType
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.catalogue.recipe_detail.recipeDetailBoundsTransform
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Recipe
import com.example.components.JustImage
import com.example.components.JustSurface
import com.example.components.theme.JustCookColorPalette

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeItem(
    collectionId: Long,
    recipe: Recipe,
    onRecipeClick: (Recipe, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    JustSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No sharedTransitionScope found")
        val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No animatedVisibilityScope found")

        with(sharedTransitionScope) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = {
                        onRecipeClick(recipe, collectionId)
                    })
                    .padding(8.dp)
            ) {
                JustImage(
                    elevation = 1.dp,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .sharedBounds(
                            rememberSharedContentState(
                                key = RecipeSharedElementKey(
                                    recipeId = recipe.id,
                                    type = RecipeSharedElementType.Image,
                                    collectionId = collectionId
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = recipeDetailBoundsTransform
                        )
                )
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = JustCookColorPalette.colors.textSecondary,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .wrapContentWidth()
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
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                            boundsTransform = recipeDetailBoundsTransform
                        )
                )
            }
        }
    }
}