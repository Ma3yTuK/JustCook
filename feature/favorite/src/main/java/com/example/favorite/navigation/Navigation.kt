package com.example.favorite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.components.composableWithCompositionLocal
import com.example.data.models.Recipe
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable
import androidx.compose.material3.Text
import com.example.data.models.MeasurementUnit
import com.example.jetsnack.ui.home.cart.Feature

@Serializable object FavoriteRoute
@Serializable object FavoritePageRoute

fun NavGraphBuilder.favoriteNavigation(
    navController: NavHostController,
    onRecipeClick: (Long) -> Unit
) {
    navigation<FavoriteRoute>(startDestination = FavoritePageRoute) {
        composableWithCompositionLocal<FavoritePageRoute> {
            Feature(
                favoriteRecipes = RecipeService.allRecipes.filter { it.isFavorite },
                onRemoveRecipe = { _ -> },
                onRecipeClick = { recipe -> onRecipeClick(recipe.id) }
            )
        }
    }
}