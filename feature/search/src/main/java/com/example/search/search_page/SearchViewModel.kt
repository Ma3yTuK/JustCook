package com.example.search.search_page

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.EntityWithId
import com.example.data.models.Image
import com.example.data.models.Ingredient
import com.example.data.models.Review
import com.example.data.models.User
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.data.models.local.SearchEntry
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.data.repositories.ImageRepository
import com.example.data.repositories.LocalSearchEntityRepository
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.auth.TokenService
import com.example.data.services.ingredient.IngredientService
import com.example.data.services.ingredient.getPagingSource
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.UploadRecipeRequest
import com.example.data.services.recipe.getPagingSource
import com.example.data.services.review.ReviewService
import com.example.data.services.review.getPagingSource
import com.example.data.services.user.UploadUserRequest
import com.example.data.services.user.UserService
import com.example.data.services.user.getPagingSource
import com.example.search.search_state.SearchState
import com.example.search.search_state.SearchType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SortingType {
    val field: String
    val display: String
    val sortingAscending: Boolean
}

enum class UserSortingType(
    override val field: String,
    override val display: String,
    override val sortingAscending: Boolean
) : SortingType {
    Name("name", "По имени", true),
    Verification("isVerified", "Сначала повара JustCook", false)
}

enum class RecipeSortingType(
    override val field: String,
    override val display: String,
    override val sortingAscending: Boolean
) : SortingType {
    Name("name", "По названию", true),
    Premium("isPremium", "Сначала премиальные", false)
}

data class SearchUiState(
    val searchState: SearchState = SearchState(),
    val query: String = "",
    val categories: List<RealCategory> = listOf(),
    val lifestyles: List<LifeStyleCategory> = listOf(),
    val recipeSortingTypes: List<RecipeSortingType> = listOf(RecipeSortingType.Name, RecipeSortingType.Premium),
    val userSortingTypes: List<UserSortingType> = listOf(UserSortingType.Name, UserSortingType.Verification),
    val searchTypes: List<SearchType> = listOf(SearchType.User, SearchType.Recipe),
    val ingredientSearchQuery: String = "",
    val userSearchQuery: String = "",
    val isLoading: Boolean = false,
    val filtersEnabled: Boolean = false,
    val ingredientFlow: Flow<PagingData<Ingredient>> = flowOf(),
    val recipeFlow: Flow<PagingData<RecipeShort>> = flowOf(),
    val userFlow: Flow<PagingData<UserShort>> = flowOf(),
    val userForRecipeFlow: Flow<PagingData<UserShort>> = flowOf(),
    val savedRecipeItems: Map<Long, RecipeShort> = mapOf(),
    val suggestions: List<SearchEntry> = listOf(),
    val showFilters: Boolean = false,
    val scrollState: LazyListState = LazyListState(),
    val searchFocused: Boolean = false
) {
    val isFilteringOrSearching: Boolean
        get() = filtersEnabled || query.isNotEmpty()
    val isSearchingForRecipe: Boolean
        get() = searchState.searchType == SearchType.Recipe
    val sortingTypes: List<SortingType>
        get() = if (isSearchingForRecipe) recipeSortingTypes else userSortingTypes
}

const val QUERY_UPDATE_INTERVAL = 700
const val SUGGESTION_LIMIT = 20

class SearchViewModel(
    private val recipeService: RecipeService,
    private val ingredientService: IngredientService,
    private val userService: UserService,
    private val suggestionRepository: LocalSearchEntityRepository,
    private val onError: (e: Throwable) -> Unit = {},
    private val onSeriousError: (e: Throwable) -> Unit = {}
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var recipePagingSource: PagingSource<Int, RecipeShort>? by mutableStateOf(null)
    private var ingredientPagingSource: PagingSource<Int, Ingredient>? by mutableStateOf(null)
    private var userPagingSource: PagingSource<Int, UserShort>? by mutableStateOf(null)
    private var userForRecipePagingSource: PagingSource<Int, UserShort>? by mutableStateOf(null)

    private var fallbackSearchState: SearchState = uiState.value.searchState

    private var cancelQueryChangeJob: () -> Unit = {}

    private val recipePager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            recipePagingSource = recipeService.getPagingSource(uiState.value.searchState, uiState.value.query)
            recipePagingSource!!
        }
    )

    private val ingredientPager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            ingredientPagingSource = ingredientService.getPagingSource(uiState.value.ingredientSearchQuery)
            ingredientPagingSource!!
        }
    )

    private val userPager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            userPagingSource = userService.getPagingSource(uiState.value.searchState, uiState.value.query)
            userPagingSource!!
        }
    )

    private val userForRecipePager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            userForRecipePagingSource = userService.getPagingSource(uiState.value.searchState, uiState.value.userSearchQuery)
            userForRecipePagingSource!!
        }
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                SearchUiState(
                    isLoading = true
                )
            }
            userPagingSource?.invalidate()
            recipePagingSource?.invalidate()
            userForRecipePagingSource?.invalidate()
            ingredientPagingSource?.invalidate()

            try {
                withContext(Dispatchers.IO) {
                    _uiState.update {
                        it.copy(
                            suggestions = suggestionRepository.getHistory(SUGGESTION_LIMIT),
                            categories = recipeService.getCategories(),
                            lifestyles = recipeService.getLifeStyles(),
                            ingredientFlow = ingredientPager.flow.cachedIn(viewModelScope),
                            recipeFlow = recipePager.flow.cachedIn(viewModelScope),
                            userFlow = userPager.flow.cachedIn(viewModelScope),
                            userForRecipeFlow = userForRecipePager.flow.cachedIn(viewModelScope),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                onSeriousErrorWrapper(e)
            }
        }
    }

    fun onSearchFocusChange(value: Boolean) {
        _uiState.update {
            it.copy(
                searchFocused = value
            )
        }
    }

    fun onFavoriteToggle(recipe: RecipeShort) {
        viewModelScope.launch {
            try {
                val newRecipe = recipe.copy(isFavorite = recipeService.makeFavorite(recipe.id))

                _uiState.update {
                    it.copy(
                        savedRecipeItems = it.savedRecipeItems + mapOf(newRecipe.id to newRecipe)
                    )
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun onShowFilters() {
        _uiState.update {
            fallbackSearchState = it.searchState
            it.copy(
                showFilters = true
            )
        }
    }

    fun onStateChange(searchState: SearchState) {
        _uiState.update {
            it.copy(
                searchState = searchState
            )
        }
    }

    fun onQueryChange(query: String) {
        cancelQueryChangeJob()
        _uiState.update {
            it.copy(
                query = query
            )
        }
        cancelQueryChangeJob = queryChangeJob {
            updateCurrentPagingSource()
        }
    }

    fun onEnter() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                val newSuggestion = (SearchEntry(entry = uiState.value.query))
                suggestionRepository.saveEntries(newSuggestion)
                _uiState.update {
                    it.copy(
                        suggestions = listOf(newSuggestion) + if (it.suggestions.size >= SUGGESTION_LIMIT) {
                            it.suggestions.subList(0, it.suggestions.size - 1)
                        } else {
                            it.suggestions
                        }
                    )
                }
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun onDismiss() {
        _uiState.update {
            it.copy(
                searchState = fallbackSearchState,
                showFilters = false,
            )
        }
        updateCurrentPagingSource()
    }

    fun onApply() {
        _uiState.update {
            it.copy(
                filtersEnabled = true,
                showFilters = false
            )
        }
        updateCurrentPagingSource()
    }

    fun onReset() {
        _uiState.update {
            fallbackSearchState = SearchState()
            it.copy(
                filtersEnabled = false,
                searchState = fallbackSearchState
            )
        }
    }

    fun onIngredientQueryChange(query: String) {
        cancelQueryChangeJob()
        _uiState.update {
            it.copy(
                ingredientSearchQuery = query
            )
        }
        cancelQueryChangeJob = queryChangeJob {
            ingredientPagingSource?.invalidate()
            _uiState.update {
                it.copy(
                    ingredientFlow = ingredientPager.flow.cachedIn(viewModelScope)
                )
            }
        }
    }

    fun onRecipeUserQueryChange(query: String) {
        cancelQueryChangeJob()
        _uiState.update {
            it.copy(
                userSearchQuery = query
            )
        }
        cancelQueryChangeJob = queryChangeJob {
            userForRecipePagingSource?.invalidate()
            _uiState.update {
                it.copy(
                    userForRecipeFlow = userForRecipePager.flow.cachedIn(viewModelScope)
                )
            }
        }
    }

    private fun onErrorWrapper(e: Throwable) {
        _uiState.update { it.copy(isLoading = false) }
        onError(e)
    }

    private fun onSeriousErrorWrapper(e: Throwable) {
        _uiState.update { it.copy(isLoading = false) }
        onSeriousError(e)
    }

    private fun queryChangeJob(onTimer: suspend () -> Unit): () -> Unit {
        var isCancelled = false

        viewModelScope.launch {
            delay(QUERY_UPDATE_INTERVAL.toLong())
            if (isCancelled)
                return@launch
            onTimer()
        }

        return { isCancelled = true }
    }

    private fun updateCurrentPagingSource() {
        _uiState.update {
            if (it.isSearchingForRecipe) {
                recipePagingSource?.invalidate()
                it.copy(
                    recipeFlow = recipePager.flow.cachedIn(viewModelScope)
                )
            } else {
                userPagingSource?.invalidate()
                it.copy(
                    userFlow = userPager.flow.cachedIn(viewModelScope)
                )
            }
        }
    }

    private fun invalidateCurrentPagingSource() {
        if (uiState.value.isSearchingForRecipe) {
            recipePagingSource?.invalidate()
        } else {
            userPagingSource?.invalidate()
        }
    }

    companion object {
        fun provideFactory(
            recipeService: RecipeService,
            ingredientService: IngredientService,
            userService: UserService,
            suggestionRepository: LocalSearchEntityRepository,
            onError: (e: Throwable) -> Unit,
            onSeriousError: (e: Throwable) -> Unit
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(
                    recipeService = recipeService,
                    ingredientService = ingredientService,
                    userService = userService,
                    suggestionRepository = suggestionRepository,
                    onError = onError,
                    onSeriousError = onSeriousError
                ) as T
            }
        }
    }
}