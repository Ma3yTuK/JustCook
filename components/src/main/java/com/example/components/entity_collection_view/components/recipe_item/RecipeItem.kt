package com.example.components.entity_collection_view.components.recipe_item

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.BoundsTransform
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
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Recipe
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.components.entity_collection_view.components.recipe_item.components.card_contentent.CardContent
import com.example.components.entity_collection_view.components.recipe_item.components.card_top.CardTop
import com.example.components.JustCard
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.short_models.RecipeShort

val HighlightCardWidth = 170.dp

data class RecipeSharedElementKey(
    val recipeId: Long,
    val type: RecipeSharedElementType,
    val collectionIndex: Int? = null
)

enum class RecipeSharedElementType {
    Bounds,
    Image,
    Title,
    Calories,
    Rating,
    Weight,
    Background
}

@OptIn(ExperimentalSharedTransitionApi::class)
val recipeDetailBoundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeItem(
    index: Int,
    collectionIndex: Int,
    recipe: RecipeShort,
    onRecipeClick: (RecipeShort, Int) -> Unit,
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
                            collectionIndex = collectionIndex
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
                            collectionIndex
                        )
                    })
                    .fillMaxSize()

            ) {
                CardTop(index, recipe, collectionIndex, gradient, scrollProvider)
                Spacer(modifier = Modifier.height(8.dp))
                CardContent(recipe, collectionIndex)
            }
        }
    }
}