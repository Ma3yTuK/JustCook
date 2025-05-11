package com.example.catalogue.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.catalogue.feed.components.recipe_collection_list.RecipeCollectionList
import com.example.components.JustSurface
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection

@Composable
fun Feed(
    recipeCollections: List<RecipeCollection>,
    onRecipeClick: (Recipe, Long) -> Unit,
    onCollectionClick: (RecipeCollection) -> Unit,
    modifier: Modifier = Modifier
) {
    JustSurface(modifier = modifier.fillMaxSize()) {
        Box {
            RecipeCollectionList(
                recipeCollections,
                onCollectionClick = onCollectionClick,
                onRecipeClick = onRecipeClick
            )
        }
    }
}