package com.example.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import kotlinx.serialization.Serializable
import com.example.search.Search

@Serializable object SearchRoute
@Serializable object SearchPageRoute

fun NavGraphBuilder.searchNavigation(
    navController: NavHostController,
    onRecipeClick: (Long) -> Unit
) {
    navigation<SearchRoute>(startDestination = SearchPageRoute) {
        composableWithCompositionLocal<SearchPageRoute> {
            Search(onRecipeClick)
        }
    }
}