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
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}