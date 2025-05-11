package com.example.catalogue.feed.components.recipe_collection_list.components.recipe_collection.components.highlight_recipe_item

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalogue.recipe_detail.RecipeSharedElementKey
import com.example.catalogue.recipe_detail.RecipeSharedElementType
import com.example.catalogue.recipe_detail.recipeDetailBoundsTransform
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Recipe
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.catalogue.feed.components.recipe_collection_list.components.recipe_collection.components.highlight_recipe_item.components.card_contentent.CardContent
import com.example.catalogue.feed.components.recipe_collection_list.components.recipe_collection.components.highlight_recipe_item.components.card_top.CardTop
import com.example.components.JustCard
import com.example.components.theme.JustCookColorPalette

val HighlightCardWidth = 170.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HighlightRecipeItem(
    index: Int,
    collectionId: Long,
    recipe: Recipe,
    onRecipeClick: (Recipe, Long) -> Unit,
    gradient: List<Color>,
    scrollProvider: () -> Float,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")

    with(sharedTransitionScope) {
        val roundedCornerAnimation by animatedVisibilityScope.transition
            .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
                when (enterExit) {
                    EnterExitState.PreEnter -> 0.dp
                    EnterExitState.Visible -> 20.dp
                    EnterExitState.PostExit -> 20.dp
                }
            }
        JustCard(
            elevation = 0.dp,
            shape = RoundedCornerShape(roundedCornerAnimation),
            modifier = modifier
                .padding(bottom = 16.dp)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = RecipeSharedElementKey(
                            recipeId = recipe.id,
                            type = RecipeSharedElementType.Bounds,
                            collectionId = collectionId
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = recipeDetailBoundsTransform,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    ),
                    enter = fadeIn(),
                    exit = fadeOut()
                )
                .size(
                    width = HighlightCardWidth,
                    height = 250.dp
                )
                .border(
                    1.dp,
                    JustCookColorPalette.colors.uiBorder.copy(alpha = 0.12f),
                    RoundedCornerShape(roundedCornerAnimation)
                )
        ) {
            Column(
                modifier = Modifier
                    .clickable(onClick = {
                        onRecipeClick(
                            recipe,
                            collectionId
                        )
                    })
                    .fillMaxSize()

            ) {
                CardTop(index, recipe, collectionId, gradient, scrollProvider)
                Spacer(modifier = Modifier.height(8.dp))
                CardContent(recipe, collectionId)
            }
        }
    }
}