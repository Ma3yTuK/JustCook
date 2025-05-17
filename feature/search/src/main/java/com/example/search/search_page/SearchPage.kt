package com.example.search.search_page

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.components.JustSurface
import com.example.components.JustDivider
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.EntityWithId
import com.example.data.models.Ingredient
import com.example.data.models.Recipe
import com.example.data.models.SortEntity
import com.example.data.models.User
import com.example.data.models.categories.Category
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.search_suggestions.SearchSuggestion
import com.example.data.models.search_suggestions.SearchSuggestionGroup
import com.example.search.search_page.components.NoResults
import com.example.search.R
import com.example.search.search_page.components.SearchCategories
import com.example.search.search_page.components.SearchResults
import com.example.search.search_page.components.SearchSuggestions
import com.example.search.search_page.components.search_bar.SearchBar
import com.example.search.search_page.components.search_bar.components.filter.Filter
import com.example.search.search_state.SearchState
import com.example.search.search_state.SearchType

@Composable
fun SearchPage(
    searchQuery: String,
    searchState: SearchState,
    searchResults: List<EntityWithId>,
    onRecipeClick: (Long) -> Unit,
    onToggleFavouriteClick: (Recipe) -> Unit,
    onUserClick: (Long) -> Unit,
    categoryCollections: List<CategoryCollection>,
    onCategoryClick: (Category) -> Unit,
    suggestions: List<SearchSuggestionGroup>,
    onStateChange: (SearchState) -> Unit,
    onQueryChange: (String) -> Unit,
    onEnter: () -> Unit,
    searching: Boolean,
    isValid: Boolean,
    onDismiss: () -> Unit,
    onResetFilters: () -> Unit,
    searchTypes: List<SearchType>,
    sortingTypes: List<SortEntity>,
    userSearchQuery: String,
    onUserQueryChange: (String) -> Unit,
    foundUsers: List<User>,
    ingredientSearchQuery: String,
    onIngredientQueryChange: (String) -> Unit,
    foundIngredients: List<Ingredient>,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var searchFocused by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    JustSurface(modifier = modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.statusBarsPadding())
            SearchBar(
                searchQuery = searchQuery,
                onShowFilters = { showFilters = true },
                onEnter = onEnter,
                searchFocused = searchFocused,
                onSearchFocusChange = { searchFocused = it },
                onQueryChange = onQueryChange,
                searching = searching
            )
            JustDivider()

            when {
                !searchFocused && !isValid -> SearchCategories(categoryCollections, onCategoryClick)
                searchFocused && !isValid -> SearchSuggestions(
                    suggestions = suggestions,
                    onSuggestionSelect = { onQueryChange(it.suggestion) }
                )
                searchResults.isNotEmpty() -> SearchResults(
                    searchResults,
                    onRecipeClick,
                    onToggleFavouriteClick,
                    onUserClick
                )
                else -> NoResults()
            }
        }

        if (showFilters) {
            focusManager.clearFocus()

            Filter(
                onDismiss = { onDismiss(); showFilters = false },
                onResetFilters = onResetFilters,
                searchState = searchState,
                onStateChange = onStateChange,
                searchTypes = searchTypes,
                sortingTypes = sortingTypes,
                categoryCollections = categoryCollections,
                userSearchQuery = userSearchQuery,
                onUserQueryChange = onUserQueryChange,
                foundUsers = foundUsers,
                ingredientSearchQuery = ingredientSearchQuery,
                onIngredientQueryChange = onIngredientQueryChange,
                foundIngredients = foundIngredients,
                isValid = isValid
            )
        }
    }
}