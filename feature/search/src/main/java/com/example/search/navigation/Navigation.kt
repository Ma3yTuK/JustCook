package com.example.search.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import com.example.data.models.categories.Category
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable
import com.example.search.search_page.SearchPage
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.models.EntityWithId
import com.example.data.models.RecipeSortingOption
import com.example.data.models.UserSortingOption
import com.example.search.search_state.SearchState
import com.example.search.search_state.SearchType

@Serializable object SearchRoute
@Serializable object SearchPageRoute

fun NavGraphBuilder.searchNavigation(
    navController: NavHostController,
    onShowRecipesByCategories: (categoryIds: List<Long>, lifeStyleIds: List<Long>) -> Unit,
    onRecipeClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {
    navigation<SearchRoute>(startDestination = SearchPageRoute) {
        composableWithCompositionLocal<SearchPageRoute> {
            var searchQuery by remember { mutableStateOf("") }
            var searchState by remember { mutableStateOf(SearchState()) }
            var searchResults: List<EntityWithId> by remember { mutableStateOf(listOf()) }
            var userSearchQuery by remember { mutableStateOf("") }
            var ingredientSearchQuery by remember { mutableStateOf("") }
            var isValid by remember { mutableStateOf(false) }

            LaunchedEffect(isValid) {
                if (isValid) {
                    searchResults =
                        if (searchState.searchType == SearchType.User) RecipeService.usersFiltered(searchState, searchQuery) else RecipeService.recipesFiltered(searchState, searchQuery)
                }
            }

            val updateValid = {
                isValid = if (searchState == SearchState() && searchQuery.isEmpty())
                    false
                else
                    true
            }

            SearchPage(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it; updateValid() },
                searchState = searchState,
                onStateChange = { searchState = it },
                searchResults = searchResults,
                onRecipeClick = onRecipeClick,
                onUserClick = onUserClick,
                onToggleFavouriteClick = { _ -> },
                categoryCollections = RecipeService.allCategoryCollections,
                onCategoryClick = { category ->
                    if (category is RealCategory) {
                        onShowRecipesByCategories(listOf(category.id), listOf())
                    }
                    if (category is LifeStyleCategory) {
                        onShowRecipesByCategories(listOf(), listOf(category.id))
                    }
                },
                suggestions = RecipeService.allSuggestionGroups,
                onEnter = {},
                onDismiss = { updateValid() },
                onResetFilters = { searchState = SearchState(); isValid = false },
                searchTypes = listOf(SearchType.Recipe, SearchType.User),
                sortingTypes = if (searchState.searchType == SearchType.User) RecipeService.userSortingOptions else RecipeService.recipeSortingOptions,
                userSearchQuery = userSearchQuery,
                onUserQueryChange = { userSearchQuery = it },
                foundUsers = RecipeService.allUsers.filter { it.name == userSearchQuery },
                ingredientSearchQuery = ingredientSearchQuery,
                onIngredientQueryChange = { ingredientSearchQuery = it },
                foundIngredients = RecipeService.allIngredients.filter { it.name == ingredientSearchQuery },
                isValid = isValid,
                searching = false
            )
        }
    }
}