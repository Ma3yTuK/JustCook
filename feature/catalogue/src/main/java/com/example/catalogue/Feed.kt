package com.example.catalogue

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.catalogue.components.RecipeCollectionList
import com.example.components.JustSurface
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection
import com.example.data.services.RecipeService

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