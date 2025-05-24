package com.example.profile.profile.myrecipes

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RecipeList
import com.example.components.RequireSignInPage
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService
import com.example.profile.R

@Composable
fun MyRecipesListPage(
    onRecipeClick: (RecipeShort) -> Unit
) {
    val recipeService = LocalRecipeService.current!!
    val tokenService = LocalTokenService.current!!
    val context = LocalContext.current

    MyRecipesListPage(
        recipeService = recipeService,
        onRecipeClick = onRecipeClick,
        onError = { Toast.makeText(context, context.resources.getString(R.string.default_feed_error_message), Toast.LENGTH_LONG).show() },
        isLoggedIn = tokenService.isSighedIn()
    )
}

@Composable
fun MyRecipesListPage(
    recipeService: RecipeService,
    onRecipeClick: (RecipeShort) -> Unit,
    onError: (e: Throwable) -> Unit,
    isLoggedIn: Boolean,
    viewModel: MyRecipesViewModel = viewModel(factory = MyRecipesViewModel.provideFactory(recipeService))
) {
    val uiState by viewModel.recipesListUiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.onError = onError
        viewModel.refresh()
    }
    JustSurface(Modifier.fillMaxSize()) {
        Column {
            PageTitle(stringResource(R.string.my_recipes))
            RecipeList(uiState.dataFlow, onRecipeClick, viewModel::onFavoriteClick, isLoggedIn, updatedItems = uiState.savedItems)
        }
    }
}