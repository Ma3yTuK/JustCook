package com.example.profile.profile.myrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.models.query_params.RecipeFiltersDefault
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.getMyRecipesPagingSource
import com.example.data.services.recipe.getPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class RecipesUiState(
    val savedItems: Map<Long, RecipeShort>,
    val dataFlow: Flow<PagingData<RecipeShort>> = emptyFlow()
)

class MyRecipesViewModel(
    private val recipeService: RecipeService,
) : ViewModel() {
    private val _recipesListUiState = MutableStateFlow(RecipesUiState(mapOf()))
    val recipesListUiState = _recipesListUiState.asStateFlow()
    private var pagingSource  = recipeService.getPagingSource(RecipeFiltersDefault(), queryParam = null)
    var onError: (e: Throwable) -> Unit = {}

    private val pager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            pagingSource = recipeService.getMyRecipesPagingSource()
            pagingSource
        }
    )

    fun refresh() {
        _recipesListUiState.update {
            it.copy(
                savedItems = mapOf()
            )
        }
        pagingSource.invalidate()
        _recipesListUiState.update {
            it.copy(
                dataFlow = pager.flow.cachedIn(viewModelScope)
            )
        }
    }

    fun onFavoriteClick(recipe: RecipeShort) {
        viewModelScope.launch {
            try {
                val newRecipe = recipe.copy(isFavorite = recipeService.makeFavorite(recipe.id))

                _recipesListUiState.update {
                    it.copy(
                        savedItems = it.savedItems + mapOf(newRecipe.id to newRecipe)
                    )
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    companion object {
        fun provideFactory(
            recipeService: RecipeService
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyRecipesViewModel(
                    recipeService = recipeService
                ) as T
            }
        }
    }
}