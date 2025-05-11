package com.example.catalogue.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.catalogue.feed.Feed
import com.example.catalogue.FilteredRecipeList
import com.example.catalogue.recipe_detail.RecipeDetail
import com.example.components.composableWithCompositionLocal
import com.example.data.models.IngredientIngredientConversion
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable

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
            var isInEditMode by remember { mutableStateOf(false) }

            RecipeDetail(
                recipe = RecipeService.allRecipes.find { it.id == recipeDetailsRoute.recipeId } ?: RecipeService.allRecipes.first(),
                collectionId = recipeDetailsRoute.collectionId,
                canDeleteReview = { _ -> true },
                onDeleteReview = { _ -> },
                onSubmitReview = { _, _ -> },
                onNameChange = { _ -> },
                onChangeDescription = { _ -> },
                getConversionsForIngredient = { _ -> listOf() },
                setIngredientAmount = { _, _ -> },
                setIngredientConversion = { _, _ -> },
                onToggleFavouritePress = {},
                isLoggedIn = true,
                isInEditMode = isInEditMode,
                canEdit = true,
                onEdit = { isInEditMode = true },
                onSaveEdit = {},
                onCancelEdit = { isInEditMode = false },
                upPress = { navController.navigateUp() },
                onDeleteIngredient = { _ -> },
                addIngredient = { _, _, _ -> },
                allIngredients = RecipeService.allIngredients
            )
        }
    }
}