package com.example.profile.profile.moderation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RecipeList
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService
import com.example.profile.R
import kotlinx.coroutines.flow.Flow

@Composable
fun ModerationPage(
    onRecipeClick: (RecipeShort) -> Unit
) {
    val recipeService = LocalRecipeService.current!!
    Moderation(
        recipeService = recipeService,
        onRecipeClick = onRecipeClick
    )
}

@Composable
fun Moderation(
    recipeService: RecipeService,
    onRecipeClick: (RecipeShort) -> Unit,
    viewModel: ModerationViewModel = viewModel(factory = ModerationViewModel.provideFactory(recipeService))
) {
    val uiState by viewModel.moderationUiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    JustSurface(Modifier.fillMaxSize()) {
        Column {
            PageTitle(stringResource(R.string.moderation))
            RecipeList(uiState.dataFlow, onRecipeClick, {}, false)
        }
    }
}