package com.example.favorite.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import com.example.data.services.RecipeServiceImpl
import com.example.favorite.favorite.FavoritePage
import kotlinx.serialization.Serializable

@Serializable object FavoriteRoute
@Serializable object FavoritePageRoute

fun NavGraphBuilder.favoriteNavigation(
    navController: NavHostController,
    onRecipeClick: (Long) -> Unit
) {
    navigation<FavoriteRoute>(startDestination = FavoritePageRoute) {
        composableWithCompositionLocal<FavoritePageRoute> {
            FavoritePage(
                onRecipeClick = { recipe -> onRecipeClick(recipe.id) }
            )
        }
    }
}