package com.example.profile.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.example.components.composableWithCompositionLocal
import com.example.data.models.Authorities
import com.example.data.services.RecipeService
import com.example.profile.profile.Profile
import kotlinx.serialization.Serializable
import com.example.profile.R
import com.example.profile.profile.Moderation

@Serializable object ProfileRoute
@Serializable object ModerationPageRoute
@Serializable object ProfilePageRoute

fun NavController.navigateToModeration(navOptions: NavOptions? = null) =
    navigate(route = ModerationPageRoute, navOptions)

fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    onCreateRecipeClick: () -> Unit,
    onModerationRecipeClick: (Long) -> Unit,
    onMyRecipesClick: (userId: Long) -> Unit
) {
    navigation<ProfileRoute>(startDestination = ProfilePageRoute) {
        composableWithCompositionLocal<ProfilePageRoute> {
            Profile(
                user = RecipeService.currentUser,
                onSaveEdit = {},
                onCancelEdit = {},
                onLogoutClick = {},
                onCreateRecipeClick = onCreateRecipeClick,
                onMyRecipesClick = { onMyRecipesClick(RecipeService.currentUser.id) },
                onImageChange = { _ -> },
                onNameChange = { _ -> },
                onModerationClick = { navController.navigateToModeration() },
                canModerate = RecipeService.currentUser.authorities.any { it.id == Authorities.Moderate.id },
                isValid = true
            )
        }
        composableWithCompositionLocal<ModerationPageRoute> {
            Moderation(
                recipes = listOf(),
                onRecipeClick = { onModerationRecipeClick(it.id) }
            )
        }
    }
}