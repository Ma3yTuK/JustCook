package com.example.catalogue.navigation

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

@Serializable object FeedRoute
@Serializable data class FilteredRecipeListRoute(val collectionId: Long)
@Serializable data class RecipeDetailRoute(val recipeId: Long, val collectionId: Long = 0)
@Serializable object Catalogue

fun NavController.navigateToFilteredRecipeList(collectionId: Long, navOptions: NavOptions? = null) =
    navigate(route = FilteredRecipeListRoute(collectionId), navOptions)

fun NavController.navigateToRecipeDetail(recipeId: Long, collectionId: Long = 0, navOptions: NavOptions? = null) =
    navigate(route = RecipeDetailRoute(recipeId, collectionId), navOptions)

fun NavGraphBuilder.catalogueNavigation(
    navController: NavHostController
) {
    navigation<Catalogue>(startDestination = FeedRoute) {
        composableWithCompositionLocal<FeedRoute> {
            Feed(
                recipeCollections = RecipeService.allCollections,
                onCollectionClick = { recipeCollection -> navController.navigateToFilteredRecipeList(recipeCollection.id) },
                onRecipeClick = { recipe, collectionId -> navController.navigateToRecipeDetail(recipe.id, collectionId) }
            )
        }
        composableWithCompositionLocal<FilteredRecipeListRoute> { backStackEntry ->
            val filteredRecipeListRoute: FilteredRecipeListRoute = backStackEntry.toRoute()

            FilteredRecipeList(
                recipes = RecipeService.allCollections.find { it.id == filteredRecipeListRoute.collectionId }?.recipes ?: RecipeService.allCollections.first().recipes,
                onItemClick = { recipe -> navController.navigateToRecipeDetail(recipe.id, filteredRecipeListRoute.collectionId) },
                onSaveClick = { _ -> }
            )
        }
        composableWithCompositionLocal<RecipeDetailRoute> { backStackEntry ->
            val recipeDetailsRoute: RecipeDetailRoute = backStackEntry.toRoute()

            RecipeDetail(
                recipe = RecipeService.allRecipes.find { it.id == recipeDetailsRoute.recipeId } ?: RecipeService.allRecipes.first(),
                collectionId = recipeDetailsRoute.collectionId,
                upPress = { navController.navigateUp() }
            )
        }
    }
}