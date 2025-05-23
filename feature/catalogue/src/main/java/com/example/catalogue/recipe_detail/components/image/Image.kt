package com.example.catalogue.recipe_detail.components.image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import android.net.Uri
import com.example.catalogue.recipe_detail.HzPadding
import com.example.catalogue.recipe_detail.MaxTitleOffset
import com.example.catalogue.recipe_detail.MinTitleOffset
import com.example.catalogue.recipe_detail.components.image.components.CollapsingImageLayout
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.JustImage
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Image(
    recipe: Recipe,
    collectionIndex: Int?,
    isInEditMode: Boolean,
    onChangeRecipeImage: (Uri) -> Unit,
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
                image = recipe.image,
                contentDescription = null,
                onImageChange = onChangeRecipeImage,
                isEditable = isInEditMode,
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
                        exit = fadeOut(),
                        enter = fadeIn(),
                        boundsTransform = recipeDetailBoundsTransform
                    )
                    .fillMaxSize()
            )
        }
    }
}