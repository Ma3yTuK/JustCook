package com.example.catalogue.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.RecipeSharedElementKey
import com.example.catalogue.RecipeSharedElementType
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.catalogue.recipeDetailBoundsTransform
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection
import com.example.data.models.CollectionType
import com.example.components.LocalSharedTransitionScope
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.theme.JustCookColorPalette

@Composable
fun RecipeCollection(
    recipeCollection: RecipeCollection,
    onRecipeClick: (Recipe, Long) -> Unit,
    onCollectionClick: (RecipeCollection) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = recipeCollection.name,
                style = MaterialTheme.typography.titleLarge,
                color = JustCookColorPalette.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            IconButton(
                onClick = { onCollectionClick(recipeCollection) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    tint = JustCookColorPalette.colors.brand,
                    contentDescription = null
                )
            }
        }
        Recipes(recipeCollection, onRecipeClick)
    }
}

@Composable
fun Recipes(
    recipeCollection: RecipeCollection,
    onRecipeClick: (Recipe, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val rowState = rememberLazyListState()
    val cardWidthWithPaddingPx = with(LocalDensity.current) { cardWidthWithPaddingPx }

    val scrollProvider = {
        // Simple calculation of scroll distance for homogenous item types with the same width.
        val offsetFromStart = cardWidthWithPaddingPx * rowState.firstVisibleItemIndex
        offsetFromStart + rowState.firstVisibleItemScrollOffset
    }

    val padding = if (recipeCollection.type == CollectionType.Highlight) 24.dp else 12.dp
    val spacing = if (recipeCollection.type == CollectionType.Highlight) 16.dp else 0.dp

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        contentPadding = PaddingValues(start = padding, end = padding)
    ) {
        itemsIndexed(recipeCollection.recipes) { index, recipe ->
            if (recipeCollection.type == CollectionType.Highlight) {
                HighlightRecipeItem(index, recipeCollection.id, recipe, onRecipeClick, JustCookColorPalette.colors.gradient6_1, scrollProvider)
            } else {
                RecipeItem(recipeCollection.id, recipe, onRecipeClick)
            }
        }
    }
}