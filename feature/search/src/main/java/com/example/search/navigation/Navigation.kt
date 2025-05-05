package com.example.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.catalogue.Feed
import com.example.catalogue.FilteredRecipeList
import com.example.catalogue.RecipeDetail
import com.example.components.composableWithCompositionLocal
import com.example.data.models.Recipe
import com.example.data.models.RecipeCollection
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable
import androidx.compose.material3.Text
import com.example.data.models.MeasurementUnit
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