package com.example.catalogue.recipe_list_page
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.catalogue.collections.CollectionDescription
import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.query_params.RecipeFiltersDefault
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.getFavoritePagingSource
import com.example.data.services.recipe.getPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class FilteredRecipeListRoute(
    override val title: String,
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf()
) : RecipeFiltersDefault(), CollectionDescription

data class RecipeUiState(
    val savedItems: Map<Long, RecipeShort>,
    val dataFlow: Flow<PagingData<RecipeShort>> = emptyFlow()
)

class RecipeListViewModel(
    private val recipeService: RecipeService,
) : ViewModel() {
    private val _recipeListUiState = MutableStateFlow(RecipeUiState(mapOf()))
    val recipeListUiState = _recipeListUiState.asStateFlow()
    private var pagingSource  = recipeService.getPagingSource(RecipeFiltersDefault(), queryParam = null)
    var onError: (e: Throwable) -> Unit = {}
    var filters: RecipeFilters = RecipeFiltersDefault()

    private val pager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            pagingSource = recipeService.getPagingSource(filters, null)
            pagingSource
        }
    )

    fun refresh() {
        _recipeListUiState.update {
            it.copy(
                savedItems = mapOf()
            )
        }
        pagingSource.invalidate()
        _recipeListUiState.update {
            it.copy(
                dataFlow = pager.flow.cachedIn(viewModelScope)
            )
        }
    }

    fun onFavoriteClick(recipe: RecipeShort) {
        viewModelScope.launch {
            try {
                val newRecipe = recipe.copy(isFavorite = recipeService.makeFavorite(recipe.id))

                _recipeListUiState.update {
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
                return RecipeListViewModel(
                    recipeService = recipeService
                ) as T
            }
        }
    }
}