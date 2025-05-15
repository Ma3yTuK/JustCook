package com.example.profile.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.example.components.RecipeListPage
import com.example.components.composableWithCompositionLocal
import com.example.data.services.RecipeService
import com.example.profile.profile.Profile
import kotlinx.serialization.Serializable
import com.example.profile.R

@Serializable object ProfileRoute
@Serializable object ProfilePageRoute
@Serializable object MyRecipesRoute

fun NavController.navigateToMyRecipes(navOptions: NavOptions? = null) =
    navigate(route = MyRecipesRoute, navOptions)

fun NavGraphBuilder.profileNavigation(
    navController: NavHostController,
    onRecipeClick: (Long) -> Unit,
    onCreateRecipeClick: () -> Unit
) {
    navigation<ProfileRoute>(startDestination = ProfilePageRoute) {
        composableWithCompositionLocal<ProfilePageRoute> {
            Profile(
                user = RecipeService.userBob,
                onSaveEdit = {},
                onCancelEdit = {},
                onLogoutClick = {},
                onCreateRecipeClick = onCreateRecipeClick,
                onMyRecipesClick = { navController.navigateToMyRecipes() },
                onImageChange = { _ -> },
                onNameChange = { _ -> },
                isValid = true
            )
        }
        composableWithCompositionLocal<MyRecipesRoute> {
            RecipeListPage(
                title = stringResource(R.string.my_recipes),
                recipes = RecipeService.allRecipes.filter { recipe -> recipe.user == RecipeService.userBob },
                onRecipeClick = { recipe -> onRecipeClick(recipe.id) },
                onFavoriteClick = { _ -> },
                true
            )
        }
    }
}