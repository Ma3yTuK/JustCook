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
import com.example.catalogue.recipe_list_page.FilteredRecipeListRoute
import com.example.catalogue.user_list_page.FilteredUserListRoute
import com.example.catalogue.recipe_list_page.RecipeListPage
import com.example.catalogue.user_list_page.UserListPage
import com.example.catalogue.recipe_detail.RecipeDetail
import com.example.catalogue.user_detail.UserDetail
import com.example.catalogue.feed.FeedPage
import com.example.catalogue.recipe_detail.RecipeDetailPage
import com.example.catalogue.user_detail.UserDetailPage
import com.example.components.PremiumAccessRequiredScreen
import com.example.components.composableWithCompositionLocal
import com.example.data.models.Authorities
import com.example.data.services.RecipeServiceImpl
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.Serializable

@Serializable object FeedRoute
@Serializable data class RecipeDetailRoute(val recipeId: Long?, val collectionIndex: Int?)
@Serializable data class UserDetailRoute(val userId: Long, val collectionIndex: Int?)
@Serializable object Catalogue

fun NavController.navigateToFilteredRecipeList(
    title: String,
    userIds: List<Long> = listOf(),
    categoryIds: List<Long> = listOf(),
    lifeStyleIds: List<Long> = listOf(),
    navOptions: NavOptions? = null
) = navigate(route = FilteredRecipeListRoute(
    title,
    userIds,
    categoryIds,
    lifeStyleIds
), navOptions)

fun NavController.navigateToFilteredUserList(
    title: String,
    isVerified: Boolean? = null,
    navOptions: NavOptions? = null
) = navigate(route = FilteredUserListRoute(
    title = title,
    isVerified = isVerified
), navOptions)

fun NavController.navigateToRecipeDetail(recipeId: Long? = null, collectionIndex: Int? = null, navOptions: NavOptions? = null) =
    navigate(route = RecipeDetailRoute(recipeId, collectionIndex), navOptions)

fun NavController.navigateToUserDetail(userId: Long, collectionIndex: Int? = null, navOptions: NavOptions? = null) =
    navigate(route = UserDetailRoute(userId, collectionIndex), navOptions)

fun NavGraphBuilder.catalogueNavigation(
    navController: NavHostController
) {
    navigation<Catalogue>(startDestination = FeedRoute) {
        composableWithCompositionLocal<FeedRoute> {
            FeedPage(
                onCollectionClick = { entityCollection -> navController.navigate(entityCollection.route) },
                onRecipeClick = { recipe, collectionIndex ->
                    navController.navigateToRecipeDetail(recipe.id, collectionIndex)
                },
                onUserClick = { user, collectionId -> navController.navigateToUserDetail(user.id, collectionId) }
            )
        }
        composableWithCompositionLocal<FilteredRecipeListRoute> { backStackEntry ->
            val filteredRecipeListRoute: FilteredRecipeListRoute = backStackEntry.toRoute()

            RecipeListPage(
                route = filteredRecipeListRoute,
                onRecipeClick = { recipe ->
                    navController.navigateToRecipeDetail(recipe.id)
                }
            )
        }
        composableWithCompositionLocal<FilteredUserListRoute> { backStackEntry ->
            val filteredUserListRoute: FilteredUserListRoute = backStackEntry.toRoute()

            UserListPage(
                route = filteredUserListRoute,
                onUserClick = { user -> navController.navigateToUserDetail(user.id) }
            )
        }
        composableWithCompositionLocal<RecipeDetailRoute> { backStackEntry ->
            val recipeDetailsRoute: RecipeDetailRoute = backStackEntry.toRoute()

            RecipeDetailPage(
                recipeId = recipeDetailsRoute.recipeId,
                collectionIndex = recipeDetailsRoute.collectionIndex,
                onUserClick = { user -> navController.navigateToUserDetail(user.id) },
                upPress = { navController.navigateUp() }
            )
        }
        composableWithCompositionLocal<UserDetailRoute> { backStackEntry ->
            val userDetailsRoute: UserDetailRoute = backStackEntry.toRoute()

            UserDetailPage(
                userId = userDetailsRoute.userId,
                collectionIndex = userDetailsRoute.collectionIndex,
                onRecipeClick = { recipe ->
                    navController.navigateToRecipeDetail(recipe.id)
                },
                upPress = { navController.navigateUp() }
            )
        }
    }
}