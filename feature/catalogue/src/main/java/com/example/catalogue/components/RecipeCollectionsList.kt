package com.example.catalogue.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.JustDivider
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection

@Composable
fun RecipeCollectionList(
    recipeCollections: List<RecipeCollection>,
    onRecipeClick: (Recipe, Long) -> Unit,
    onCollectionClick: (RecipeCollection) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(recipeCollections) { index, recipeCollection ->
            if (index > 0) {
                JustDivider(thickness = 2.dp)
            }

            RecipeCollection(
                recipeCollection = recipeCollection,
                onCollectionClick = onCollectionClick,
                onRecipeClick = onRecipeClick
            )
        }
    }
}