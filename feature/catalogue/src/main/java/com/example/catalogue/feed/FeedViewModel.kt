package com.example.catalogue.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.catalogue.recipe_list_page.FilteredRecipeListRoute
import com.example.catalogue.user_list_page.FilteredUserListRoute
import com.example.catalogue.collections.EntityCollection
import com.example.catalogue.collections.RecipeCollection
import com.example.catalogue.collections.UserCollection
import com.example.data.models.query_params.PageParams
import com.example.data.services.recipe.RecipeService
import com.example.data.services.user.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedUiState(
    val entityCollections: List<EntityCollection> = listOf(
        RecipeCollection(
            route = FilteredRecipeListRoute(
                title = "Простые блюда",
                categoryIds = listOf(19)
            ),
            entities = listOf()
        ),
        UserCollection(
            route = FilteredUserListRoute(
                title = "Повара JustCook",
                isVerified = true
            ),
            entities = listOf()
        ),
        RecipeCollection(
            route = FilteredRecipeListRoute(
                title = "Закуски",
                categoryIds = listOf(5)
            ),
            entities = listOf()
        )
    )
)

class FeedViewModel(
    private val recipeService: RecipeService,
    private val userService: UserService,
) : ViewModel() {
    private val _feedUiState = MutableStateFlow(FeedUiState())
    val feedUiState = _feedUiState.asStateFlow()

    var isLoading by mutableStateOf(false)
    var onError: (e: Throwable) -> Unit = {}

    fun refresh() {
        viewModelScope.launch {
            isLoading = true
            var isError = false
            val result: MutableList<EntityCollection> = feedUiState.value.entityCollections.toMutableStateList()
            val job1 = launch {
                try {
                    val entityCollection = feedUiState.value.entityCollections[0]
                    result[0] = ((entityCollection as RecipeCollection).copy(entities = recipeService.getAllRecipes(PageParams(0, 10).toMap() + (entityCollection.route as FilteredRecipeListRoute).toMap()).content))
                } catch (e: Exception) {
                    onError(e)
                    isError = true
                }
            }
            val job2 = launch {
                try {
                    val entityCollection = feedUiState.value.entityCollections[1]
                    result[1] = ((entityCollection as UserCollection).copy(entities = userService.getAllUsers(PageParams(0, 10).toMap() + (entityCollection.route as FilteredUserListRoute).toMap()).content))
                } catch (e: Exception) {
                    onError(e)
                    isError = true
                }
            }
            val job3 = launch {
                try {
                    val entityCollection = feedUiState.value.entityCollections[2]
                    result[2] = ((entityCollection as RecipeCollection).copy(entities = recipeService.getAllRecipes(PageParams(0, 10).toMap() + (entityCollection.route as FilteredRecipeListRoute).toMap()).content))
                } catch (e: Exception) {
                    onError(e)
                    isError = true
                }
            }
            job1.join()
            job2.join()
            job3.join()
            if (!isError) {
                _feedUiState.update {
                    it.copy(
                        entityCollections = result
                    )
                }
            }
            isLoading = false
        }
    }

    companion object {
        fun provideFactory(
            recipeService: RecipeService,
            userService: UserService,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(
                    recipeService = recipeService,
                    userService = userService
                ) as T
            }
        }
    }
}