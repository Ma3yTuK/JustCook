package com.example.catalogue.components

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.RecipeSharedElementKey
import com.example.catalogue.RecipeSharedElementType
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.catalogue.recipeDetailBoundsTransform
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Recipe
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Density
import com.example.components.RecipeImage
import com.example.components.JustCard
import com.example.components.JustSurface
import com.example.components.theme.JustCookColorPalette

val HighlightCardWidth = 170.dp
val HighlightCardPadding = 16.dp
val Density.cardWidthWithPaddingPx
    get() = (HighlightCardWidth + HighlightCardPadding).toPx()

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
                Box(
                    modifier = Modifier
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
                                        collectionId = collectionId
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

                    RecipeImage(
                        contentDescription = null,
                        elevation = 1.dp,
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = RecipeSharedElementKey(
                                        recipeId = recipe.id,
                                        type = RecipeSharedElementType.Image,
                                        collectionId = collectionId
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

                Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recipe.tagline,
                    style = MaterialTheme.typography.bodyLarge,
                    color = JustCookColorPalette.colors.textHelp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .sharedBounds(
                            rememberSharedContentState(
                                key = RecipeSharedElementKey(
                                    recipeId = recipe.id,
                                    type = RecipeSharedElementType.Tagline,
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
    }
}

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
                RecipeImage(
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

fun Modifier.offsetGradientBackground(
    colors: List<Color>,
    width: Density.() -> Float,
    offset: Density.() -> Float = { 0f }
) = drawBehind {
    val actualOffset = offset()

    drawRect(
        Brush.horizontalGradient(
            colors = colors,
            startX = -actualOffset,
            endX = width() - actualOffset,
            tileMode = TileMode.Mirror
        )
    )
}