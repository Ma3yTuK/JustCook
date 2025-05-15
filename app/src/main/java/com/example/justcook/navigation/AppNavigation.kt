package com.example.justcook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.example.catalogue.navigation.catalogueNavigation
import com.example.catalogue.navigation.Catalogue
import com.example.catalogue.navigation.navigateToRecipeDetail
import com.example.favorite.navigation.favoriteNavigation
import com.example.profile.navigation.profileNavigation
import com.example.search.navigation.searchNavigation

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Catalogue,
        modifier = modifier
    ) {
        catalogueNavigation(
            navController
        )
        searchNavigation(
            navController,
            onRecipeClick = { recipeId -> navController.navigateToRecipeDetail(recipeId) }
        )
        favoriteNavigation(
            navController,
            onRecipeClick = { recipeId -> navController.navigateToRecipeDetail(recipeId) }
        )
        profileNavigation(
            navController,
            onRecipeClick = { recipeId -> navController.navigateToRecipeDetail(recipeId) },
            onCreateRecipeClick = { navController.navigateToRecipeDetail() }
        )
    }
}