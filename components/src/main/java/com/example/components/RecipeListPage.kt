package com.example.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.data.models.Recipe

@Composable
fun RecipeListPage(
    title: String,
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onFavoriteClick: (Recipe) -> Unit,
    isLoggedIn: Boolean
) {
    JustSurface(Modifier.fillMaxSize()) {
        Column {
            PageTitle(title)
            RecipeList(recipes, onRecipeClick, onFavoriteClick, isLoggedIn)
        }
    }
}