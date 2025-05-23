package com.example.catalogue.recipe_detail.components.title.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.AMOUNT_OF_GRAM_FOR_CALORIES
import com.example.catalogue.recipe_detail.HzPadding
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeCalories(
    recipe: Recipe,
    collectionIndex: Int?
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalArgumentException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")

    with(sharedTransitionScope) {
        Text(
            text = "${if (recipe.weight != 0L) recipe.calories * AMOUNT_OF_GRAM_FOR_CALORIES / recipe.weight else 0} ${stringResource(R.string.calories)}",
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.titleSmall,
            fontSize = 20.sp,
            color = JustCookColorPalette.colors.textHelp,
            modifier = HzPadding
                .sharedBounds(
                    rememberSharedContentState(
                        key = RecipeSharedElementKey(
                            recipeId = recipe.id,
                            type = RecipeSharedElementType.Calories,
                            collectionIndex = collectionIndex
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = recipeDetailBoundsTransform
                )
                .wrapContentWidth()
        )
    }
}