package com.example.components.entity_collection_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.example.components.entity_collection_view.components.UserItem
import com.example.components.entity_collection_view.components.recipe_item.RecipeItem
import com.example.components.entity_collection_view.components.recipe_item.components.card_top.cardWidthWithPaddingPx
import com.example.data.models.Recipe
import com.example.data.models.collections.RecipeCollection
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.User
import com.example.data.models.collections.EntityCollection
import com.example.data.models.collections.UserCollection

@Composable
fun EntityCollectionView(
    entityCollection: EntityCollection,
    onCollectionClick: (EntityCollection) -> Unit,
    modifier: Modifier = Modifier,
    onRecipeClick: (Recipe, Long) -> Unit = { _, _ -> },
    onUserClick: (User, Long) -> Unit = { _, _ -> },
    collectionName: String = entityCollection.name
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = collectionName,
                style = MaterialTheme.typography.titleLarge,
                color = JustCookColorPalette.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            IconButton(
                onClick = { onCollectionClick(entityCollection) },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    tint = JustCookColorPalette.colors.brand,
                    contentDescription = null
                )
            }
        }
        Entities(entityCollection, onRecipeClick, onUserClick)
    }
}

@Composable
fun Entities(
    entityCollection: EntityCollection,
    onRecipeClick: (Recipe, Long) -> Unit,
    onUserClick: (User, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val rowState = rememberLazyListState()
    val cardWidthWithPaddingPx = with(LocalDensity.current) { cardWidthWithPaddingPx }

    val scrollProvider = {
        // Simple calculation of scroll distance for homogenous item types with the same width.
        val offsetFromStart = cardWidthWithPaddingPx * rowState.firstVisibleItemIndex
        offsetFromStart + rowState.firstVisibleItemScrollOffset
    }

    if (entityCollection is RecipeCollection) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
        ) {
            itemsIndexed(entityCollection.entities) { index, recipe ->
                RecipeItem(
                    index,
                    entityCollection.id,
                    recipe,
                    onRecipeClick,
                    if (recipe.isPremium) JustCookColorPalette.colors.gradientGold else JustCookColorPalette.colors.gradient6_1,
                    scrollProvider
                )
            }
        }
    }
    if (entityCollection is UserCollection) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(entityCollection.entities) { entity ->
                UserItem(entityCollection.id, entity, onUserClick)
            }
        }
    }
}