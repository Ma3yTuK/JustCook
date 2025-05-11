package com.example.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import com.example.data.services.RecipeService
import com.example.profile.profile.Profile
import kotlinx.serialization.Serializable

@Serializable object ProfileRoute
@Serializable object ProfilePageRoute

fun NavGraphBuilder.profileNavigation(
    navController: NavHostController
) {
    navigation<ProfileRoute>(startDestination = ProfilePageRoute) {
        composableWithCompositionLocal<ProfilePageRoute> {
            Profile(
                user = RecipeService.userBob,
                onLogoutClick = {},
                onFavoritesClick = {},
                onMyRecipesClick = {},
                onEditClick = {}
            )
        }
    }
}