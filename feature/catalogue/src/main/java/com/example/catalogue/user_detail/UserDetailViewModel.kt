package com.example.catalogue.user_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.catalogue.collections.CollectionDescription
import com.example.data.models.User
import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.query_params.RecipeFiltersDefault
import com.example.data.models.query_params.UserFilters
import com.example.data.models.query_params.UserFiltersDefault
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.getFavoritePagingSource
import com.example.data.services.recipe.getPagingSource
import com.example.data.services.user.UserService
import com.example.data.services.user.getPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

data class UserDetailUiState(
    val dataFlow: Flow<PagingData<RecipeShort>> = emptyFlow(),
    val user: User,
    val savedItems: Map<Long, RecipeShort>,
    val isLoading: Boolean = false,
)

class UserDetailViewModel(
    private val userService: UserService,
    private val recipeService: RecipeService,
) : ViewModel() {
    private val _userDetailUiState = MutableStateFlow(UserDetailUiState(emptyFlow(), User(-1, "", "", false, listOf()), mapOf()))
    val userDetailUiState = _userDetailUiState.asStateFlow()
    private var pagingSource  = recipeService.getPagingSource(RecipeFiltersDefault(), null)
    var onError: (e: Throwable) -> Unit = {}

    private val pager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            val user = _userDetailUiState.value.user
            pagingSource = recipeService.getPagingSource(RecipeFiltersDefault(users = setOf(
                UserShort(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    isVerified = user.isVerified,
                    image = user.image
                )
            )), null)
            pagingSource
        }
    )

    fun refresh(userId: Long) {
        _userDetailUiState.update {
            it.copy(
                isLoading = true,
                savedItems = mapOf()
            )
        }
        pagingSource.invalidate()
        viewModelScope.launch {
            try {
                _userDetailUiState.update {
                    val newUser = userService.getUser(userId)
                    it.copy(
                        user = newUser,
                        dataFlow = pager.flow.cachedIn(viewModelScope),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    fun onFavoriteClick(recipe: RecipeShort) {
        viewModelScope.launch {
            try {
                val newRecipe = recipe.copy(isFavorite = recipeService.makeFavorite(recipe.id))

                _userDetailUiState.update {
                    it.copy(
                        savedItems = it.savedItems + mapOf(newRecipe.id to newRecipe)
                    )
                }
            } catch (e: Exception) {
                onErrorWrapper(e)
            }
        }
    }

    private fun onErrorWrapper(e: Throwable) {
        onError(e)
        _userDetailUiState.update {
            it.copy(
                isLoading = false
            )
        }
    }

    companion object {
        fun provideFactory(
            userService: UserService,
            recipeService: RecipeService
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserDetailViewModel(
                    userService = userService,
                    recipeService = recipeService
                ) as T
            }
        }
    }
}