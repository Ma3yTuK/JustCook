package com.example.favorite.favorite

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.Image
import com.example.data.models.User
import com.example.data.models.short_models.RecipeShort
import com.example.data.repositories.ImageRepository
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.MyPagingSource
import com.example.data.services.RecipeServiceImpl
import com.example.data.services.auth.TokenService
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.UploadRecipeRequest
import com.example.data.services.recipe.getFavoritePagingSource
import com.example.data.services.user.UploadUserRequest
import com.example.data.services.user.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class FavoriteUiState(
    val removedItems: Set<Long>,
    var dataFlow: Flow<PagingData<RecipeShort>> = emptyFlow()
)

class FavoriteViewModel(
    private val recipeService: RecipeService,
) : ViewModel() {
    private val _favoriteUiState = MutableStateFlow(FavoriteUiState(setOf()))
    val favoriteUiState = _favoriteUiState.asStateFlow()
    private var pagingSource  = recipeService.getFavoritePagingSource()
    var onError: (e: Throwable) -> Unit = {}

    private val pager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            pagingSource = recipeService.getFavoritePagingSource()
            pagingSource
        }
    )

    fun refresh() {
        _favoriteUiState.update {
            it.copy(
                removedItems = setOf()
            )
        }
        pagingSource.invalidate()
        _favoriteUiState.update {
            it.copy(
                dataFlow = pager.flow.cachedIn(viewModelScope)
            )
        }
    }

    fun onRemoveRecipe(recipe: RecipeShort) {
        viewModelScope.launch {
            try {
                val newRecipe = recipe.copy(isFavorite = recipeService.makeFavorite(recipe.id))

                _favoriteUiState.update {
                    it.copy(
                        removedItems = it.removedItems + setOf(newRecipe.id)
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
                return FavoriteViewModel(
                    recipeService = recipeService
                ) as T
            }
        }
    }
}