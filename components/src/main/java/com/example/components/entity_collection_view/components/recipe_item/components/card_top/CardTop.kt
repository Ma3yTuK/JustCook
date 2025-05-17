package com.example.components.entity_collection_view.components.recipe_item.components.card_top

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Recipe
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Density
import com.example.components.entity_collection_view.components.recipe_item.HighlightCardWidth
import com.example.components.JustImage
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.components.modifiers.offsetGradientBackground

val HighlightCardPadding = 16.dp
val Density.cardWidthWithPaddingPx
    get() = (HighlightCardWidth + HighlightCardPadding).toPx()

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardTop(
    index: Int,
    recipe: Recipe,
    collectionIndex: Int,
    gradient: List<Color>,
    scrollProvider: () -> Float,
    modifier: Modifier = Modifier
) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .height(160.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .sharedBounds(
                        rememberSharedContentState(
                            key = RecipeSharedElementKey(
                                recipeId = recipe.id,
                                type = RecipeSharedElementType.Background,
                                collectionIndex = collectionIndex
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = recipeDetailBoundsTransform,
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )
                    .height(100.dp)
                    .fillMaxWidth()
                    .offsetGradientBackground(
                        colors = gradient,
                        width = {
                            // The Cards show a gradient which spans 6 cards and
                            // scrolls with parallax.
                            6 * cardWidthWithPaddingPx
                        },
                        offset = {
                            val left = index * cardWidthWithPaddingPx
                            val gradientOffset = left - (scrollProvider() / 3f)
                            gradientOffset
                        }
                    )
            )

            JustImage(
                contentDescription = null,
                elevation = 1.dp,
                modifier = Modifier
                    .sharedBounds(
                        rememberSharedContentState(
                            key = RecipeSharedElementKey(
                                recipeId = recipe.id,
                                type = RecipeSharedElementType.Image,
                                collectionIndex = collectionIndex
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                        boundsTransform = recipeDetailBoundsTransform
                    )
                    .align(Alignment.BottomCenter)
                    .size(120.dp)
            )
        }
    }
}