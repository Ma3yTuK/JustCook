package com.example.search.search_page

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import com.example.components.JustSurface
import com.example.components.JustDivider
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.EntityWithId
import com.example.data.models.Ingredient
import com.example.data.models.Recipe
import com.example.data.models.User
import com.example.data.models.categories.Category
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.short_models.RecipeShort
import com.example.search.search_page.components.NoResults
import com.example.search.R
import com.example.search.search_page.components.SearchCategories
import com.example.search.search_page.components.SearchResults
import com.example.search.search_page.components.SearchSuggestions
import com.example.search.search_page.components.search_bar.SearchBar
import com.example.search.search_page.components.search_bar.components.filter.Filter
import com.example.search.search_state.SearchState
import com.example.search.search_state.SearchType
import kotlinx.coroutines.flow.Flow
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.catalogue.recipe_detail.RecipeDetail
import com.example.components.ErrorPage
import com.example.components.LoadingOverlay
import com.example.data.repositories.LocalImageRepository
import com.example.data.repositories.LocalSearchEntityRepository
import com.example.data.repositories.LocalSuggestionRepository
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.ingredient.IngredientService
import com.example.data.services.ingredient.LocalIngredientService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService
import com.example.data.services.review.LocalReviewService
import com.example.data.services.user.LocalUserService
import com.example.data.services.user.UserService

@Composable
fun SearchPage(
    onRecipeClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit,
    onCategoryClick: (Category) -> Unit,
) {
    val recipeService = LocalRecipeService.current!!
    val userService = LocalUserService.current!!
    val suggestionRepository = LocalSuggestionRepository.current!!
    val ingredientService = LocalIngredientService.current!!
    val context = LocalContext.current

    var isSeriousError by remember { mutableStateOf(false) }

    if (isSeriousError) {
        ErrorPage(stringResource(R.string.default_feed_error_message))
    } else {
        SearchPage(
            recipeService = recipeService,
            userService = userService,
            ingredientService = ingredientService,
            suggestionRepository = suggestionRepository,
            onCategoryClick = onCategoryClick,
            onRecipeClick = onRecipeClick,
            onUserClick = onUserClick,
            onError = { Toast.makeText(context, context.resources.getString(R.string.default_feed_error_message), Toast.LENGTH_LONG).show() },
            onSeriousError = { isSeriousError = true }
        )
    }
}

@Composable
fun SearchPage(
    recipeService: RecipeService,
    ingredientService: IngredientService,
    userService: UserService,
    suggestionRepository: LocalSearchEntityRepository,
    onRecipeClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onError: (e: Throwable) -> Unit,
    onSeriousError: (e: Throwable) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.provideFactory(
        recipeService = recipeService,
        ingredientService = ingredientService,
        userService = userService,
        suggestionRepository = suggestionRepository,
        onError = onError,
        onSeriousError = onSeriousError
    ))
) {
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchResult = if (uiState.isSearchingForRecipe) uiState.recipeFlow.collectAsLazyPagingItems() else uiState.userFlow.collectAsLazyPagingItems()
    val loadState = searchResult.loadState

    JustSurface(modifier = modifier.fillMaxSize()) {
        LoadingOverlay(uiState.isLoading) {
            Column {
                Spacer(modifier = Modifier.statusBarsPadding())
                SearchBar(
                    searchQuery = uiState.query,
                    onShowFilters = viewModel::onShowFilters,
                    onEnter = viewModel::onEnter,
                    searchFocused = uiState.searchFocused,
                    onSearchFocusChange = viewModel::onSearchFocusChange,
                    onQueryChange = viewModel::onQueryChange,
                    searching = !loadState.isIdle && !loadState.hasError
                )
                JustDivider()

                when {
                    !uiState.searchFocused && !uiState.isFilteringOrSearching -> SearchCategories(
                        categories = uiState.categories,
                        lifestyles = uiState.lifestyles,
                        onCategoryClick
                    )

                    uiState.searchFocused && !uiState.isFilteringOrSearching -> SearchSuggestions(
                        suggestions = uiState.suggestions,
                        onSuggestionSelect = { viewModel.onQueryChange(it.entry) }
                    )

                    (!loadState.isIdle || searchResult.itemCount > 0) && !loadState.hasError -> SearchResults(
                        searchResult,
                        uiState.savedRecipeItems,
                        onRecipeClick,
                        viewModel::onFavoriteToggle,
                        onUserClick,
                        uiState.scrollState,
                    )
                    else -> NoResults(loadState.hasError)
                }
            }

            if (uiState.showFilters) {
                keyboardController?.hide()

                Filter(
                    onDismiss = viewModel::onDismiss,
                    onApply = viewModel::onApply,
                    onResetFilters = viewModel::onReset,
                    searchState = uiState.searchState,
                    onStateChange = viewModel::onStateChange,
                    searchTypes = uiState.searchTypes,
                    sortingTypes = uiState.sortingTypes,
                    categories = uiState.categories,
                    lifestyles = uiState.lifestyles,
                    userSearchQuery = uiState.userSearchQuery,
                    onUserQueryChange = viewModel::onRecipeUserQueryChange,
                    foundUsers = uiState.userForRecipeFlow,
                    ingredientSearchQuery = uiState.ingredientSearchQuery,
                    onIngredientQueryChange = viewModel::onIngredientQueryChange,
                    foundIngredients = uiState.ingredientFlow,
                )
            }
        }
    }
}