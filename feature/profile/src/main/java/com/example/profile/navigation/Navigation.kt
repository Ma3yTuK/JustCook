package com.example.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import kotlinx.serialization.Serializable
import com.example.profile.profile.moderation.Moderation
import com.example.profile.profile.moderation.ModerationPage
import com.example.profile.profile.profile.ProfilePage
import kotlinx.coroutines.flow.flowOf

@Serializable object ProfileRoute
@Serializable object ModerationPageRoute
@Serializable object ProfilePageRoute

fun NavController.navigateToModeration(navOptions: NavOptions? = null) =
    navigate(route = ModerationPageRoute, navOptions)

fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    onCreateRecipeClick: () -> Unit,
    onModerationRecipeClick: (Long) -> Unit,
    onUserRecipesClick: (userId: Long) -> Unit
) {
    navigation<ProfileRoute>(startDestination = ProfilePageRoute) {
        composableWithCompositionLocal<ProfilePageRoute> {
            ProfilePage(
                onCreateRecipeClick = onCreateRecipeClick,
                onUserRecipesClick = onUserRecipesClick,
                onModerationClick = { navController.navigateToModeration() }
            )
        }
        composableWithCompositionLocal<ModerationPageRoute> {
            ModerationPage(
                onRecipeClick = { onModerationRecipeClick(it.id) }
            )
        }
    }
}