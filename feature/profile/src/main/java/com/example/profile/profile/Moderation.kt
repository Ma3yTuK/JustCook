package com.example.profile.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RecipeList
import com.example.data.models.Recipe
import com.example.profile.R

@Composable
fun Moderation(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    JustSurface(Modifier.fillMaxSize()) {
        Column {
            PageTitle(stringResource(R.string.moderation))
            RecipeList(recipes, onRecipeClick, {}, false)
        }
    }
}