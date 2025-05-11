package com.example.catalogue.recipe_detail.components.image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.example.catalogue.recipe_detail.HzPadding
import com.example.catalogue.recipe_detail.MaxTitleOffset
import com.example.catalogue.recipe_detail.MinTitleOffset
import com.example.catalogue.recipe_detail.RecipeSharedElementKey
import com.example.catalogue.recipe_detail.RecipeSharedElementType
import com.example.catalogue.recipe_detail.components.image.components.CollapsingImageLayout
import com.example.catalogue.recipe_detail.recipeDetailBoundsTransform
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.JustImage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Image(
    recipeId: Long,
    collectionId: Long,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.statusBarsPadding()
    ) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No sharedTransitionScope found")
        val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No animatedVisibilityScope found")

        with(sharedTransitionScope) {
            JustImage(
                contentDescription = null,
                modifier = Modifier
                    .sharedBounds(
                        rememberSharedContentState(
                            key = RecipeSharedElementKey(
                                recipeId = recipeId,
                                type = RecipeSharedElementType.Image,
                                collectionId = collectionId
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        exit = fadeOut(),
                        enter = fadeIn(),
                        boundsTransform = recipeDetailBoundsTransform
                    )
                    .fillMaxSize()
            )
        }
    }
}