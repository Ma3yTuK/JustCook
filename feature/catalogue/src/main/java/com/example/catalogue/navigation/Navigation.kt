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
import com.example.components.RecipeListPage
import com.example.catalogue.UserListPage
import com.example.catalogue.Feed
import com.example.catalogue.recipe_detail.RecipeDetail
import com.example.catalogue.UserDetail
import com.example.components.PremiumAccessRequiredScreen
import com.example.components.composableWithCompositionLocal
import com.example.data.models.Recipe
import com.example.data.models.collections.RecipeCollection
import com.example.data.services.RecipeService
import kotlinx.serialization.Serializable

@Serializable object FeedRoute
@Serializable data class FilteredRecipeListRoute(val collectionId: Long)
@Serializable data class FilteredUserListRoute(val collectionId: Long)
@Serializable data class RecipeDetailRoute(val recipeId: Long? = null, val collectionId: Long = 0)
@Serializable data class UserDetailRoute(val userId: Long, val collectionId: Long = 0)
@Serializable object Catalogue

fun NavController.navigateToFilteredRecipeList(collectionId: Long, navOptions: NavOptions? = null) =
    navigate(route = FilteredRecipeListRoute(collectionId), navOptions)

fun NavController.navigateToFilteredUserList(collectionId: Long, navOptions: NavOptions? = null) =
    navigate(route = FilteredUserListRoute(collectionId), navOptions)

fun NavController.navigateToRecipeDetail(recipeId: Long? = null, collectionId: Long = 0, navOptions: NavOptions? = null) =
    navigate(route = RecipeDetailRoute(recipeId, collectionId), navOptions)

fun NavController.navigateToUserDetail(userId: Long, collectionId: Long = 0, navOptions: NavOptions? = null) =
    navigate(route = UserDetailRoute(userId, collectionId), navOptions)

fun NavGraphBuilder.catalogueNavigation(
    navController: NavHostController
) {
    navigation<Catalogue>(startDestination = FeedRoute) {
        composableWithCompositionLocal<FeedRoute> {
            Feed(
                collections = RecipeService.allCollections,
                onCollectionClick = { entityCollection -> if (entityCollection is RecipeCollection) navController.navigateToFilteredRecipeList(entityCollection.id) else navController.navigateToFilteredUserList(entityCollection.id) },
                onRecipeClick = { recipe, collectionId ->
                    navController.navigateToRecipeDetail(recipe.id, collectionId)
                },
                onUserClick = { user, collectionId -> navController.navigateToUserDetail(user.id, collectionId) }
            )
        }
        composableWithCompositionLocal<FilteredRecipeListRoute> { backStackEntry ->
            val filteredRecipeListRoute: FilteredRecipeListRoute = backStackEntry.toRoute()
            val recipeCollection = RecipeService.allRecipeCollections.find { it.id == filteredRecipeListRoute.collectionId } ?: RecipeService.allRecipeCollections.first()

            RecipeListPage(
                title = recipeCollection.name,
                recipes = recipeCollection.entities,
                onRecipeClick = { recipe ->
                    navController.navigateToRecipeDetail(recipe.id, filteredRecipeListRoute.collectionId)
                },
                onFavoriteClick = { _ -> },
                isLoggedIn = true
            )
        }
        composableWithCompositionLocal<FilteredUserListRoute> { backStackEntry ->
            val filteredUserListRoute: FilteredUserListRoute = backStackEntry.toRoute()
            val userCollection = RecipeService.allUserCollections.find { it.id == filteredUserListRoute.collectionId } ?: RecipeService.allUserCollections.first()

            UserListPage(
                title = userCollection.name,
                users = userCollection.entities,
                onUserClick = { user -> navController.navigateToUserDetail(user.id, filteredUserListRoute.collectionId) }
            )
        }
        composableWithCompositionLocal<RecipeDetailRoute> { backStackEntry ->
            val recipeDetailsRoute: RecipeDetailRoute = backStackEntry.toRoute()
            var isInEditMode by remember { mutableStateOf(recipeDetailsRoute.recipeId == null) }

            val recipe = RecipeService.allRecipes.firstOrNull { it.id == recipeDetailsRoute.recipeId } ?: Recipe(0, "Новый рецепт", 0, 0f, "", false, false, RecipeService.userBob, listOf(), listOf(), listOf())

            if (recipe.isPremium) {
                PremiumAccessRequiredScreen(
                    onGoBack = { navController.navigateUp() }
                )
            } else {
                RecipeDetail(
                    recipe = recipe,
                    collectionId = recipeDetailsRoute.collectionId,
                    canDeleteReview = { _ -> true },
                    onDeleteReview = { _ -> },
                    onSubmitReview = { _, _ -> },
                    onUserClick = { user -> navController.navigateToUserDetail(user.id) },
                    onNameChange = { _ -> },
                    onChangeDescription = { _ -> },
                    getConversionsForIngredient = { _ -> listOf() },
                    setIngredientAmount = { _, _ -> },
                    setIngredientConversion = { _, _ -> },
                    onToggleFavoritePress = {},
                    isLoggedIn = true,
                    isInEditMode = isInEditMode,
                    canEdit = true,
                    onEdit = { isInEditMode = true },
                    onSaveEdit = {},
                    onCancelEdit = { isInEditMode = false },
                    upPress = { navController.navigateUp() },
                    onDeleteIngredient = { _ -> },
                    addIngredient = { _, _, _ -> },
                    setStepDescription = { _, _ -> },
                    onDeleteStep = { _ -> },
                    addStep = { _ -> },
                    setStepImage = { _, _ -> },
                    onChangeRecipeImage = { _ -> },
                    allIngredients = RecipeService.allIngredients,
                    isValid = true
                )
            }
        }
        composableWithCompositionLocal<UserDetailRoute> { backStackEntry ->
            val userDetailsRoute: UserDetailRoute = backStackEntry.toRoute()

            UserDetail(
                user = RecipeService.allUsers.find { it.id == userDetailsRoute.userId } ?: RecipeService.allUsers.first(),
                collectionId = userDetailsRoute.collectionId,
                recipes = RecipeService.allRecipes.filter { it.user.id == userDetailsRoute.userId },
                onRecipeClick = { recipe ->
                    navController.navigateToRecipeDetail(recipe.id)
                },
                upPress = { navController.navigateUp() },
                onFavoriteClick = { _ -> },
                isLoggedIn = true
            )
        }
    }
}