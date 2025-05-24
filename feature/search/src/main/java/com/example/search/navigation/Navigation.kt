package com.example.search.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import kotlinx.serialization.Serializable
import com.example.search.search_page.SearchPage
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.example.data.models.EntityWithId
import com.example.search.search_state.SearchState
import com.example.search.search_state.SearchType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.Flow

@Serializable object SearchRoute
@Serializable object SearchPageRoute

fun NavGraphBuilder.searchNavigation(
    navController: NavHostController,
    onShowRecipesByCategories: (category: RealCategory?, lifestyle: LifeStyleCategory?) -> Unit,
    onRecipeClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {
    navigation<SearchRoute>(startDestination = SearchPageRoute) {
        composableWithCompositionLocal<SearchPageRoute> {
            SearchPage(
                onRecipeClick = onRecipeClick,
                onUserClick = onUserClick,
                onCategoryClick = { category ->
                    if (category is RealCategory) {
                        onShowRecipesByCategories(category, null)
                    }
                    if (category is LifeStyleCategory) {
                        onShowRecipesByCategories(null, category)
                    }
                },
            )
        }
    }
}