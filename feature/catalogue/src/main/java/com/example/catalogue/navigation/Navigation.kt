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
import com.example.catalogue.RecipeListPage
import com.example.catalogue.UserListPage
import com.example.catalogue.Feed
import com.example.catalogue.recipe_detail.RecipeDetail
import com.example.catalogue.UserDetail
import com.example.catalogue.collections.CollectionDescription
import com.example.catalogue.collections.RecipeCollection
import com.example.catalogue.collections.UserCollection
import com.example.components.PremiumAccessRequiredScreen
import com.example.components.composableWithCompositionLocal
import com.example.data.models.Recipe
import com.example.data.models.RecipeFilters
import com.example.data.models.RecipeFiltersDefault
import com.example.data.models.User
import com.example.data.models.UserFilters
import com.example.data.models.UserFiltersDefault
import com.example.data.services.RecipeService
import com.example.data.services.RecipeService.omelet
import com.example.data.services.RecipeService.pancakes
import com.example.data.services.RecipeService.userAlice
import com.example.data.services.RecipeService.userCarol
import kotlinx.serialization.Serializable

@Serializable object FeedRoute
@Serializable data class RecipeDetailRoute(val recipeId: Long?, val collectionIndex: Int?, val moderation: Boolean)
@Serializable data class UserDetailRoute(val userId: Long, val collectionIndex: Int?)
@Serializable object Catalogue

@Serializable data class FilteredRecipeListRoute(
    override val title: String,
    override val sortingOptionId: Long?,
    override val userIds: List<Long> = listOf(),
    override val categoryIds: List<Long> = listOf(),
    override val lifeStyleIds: List<Long> = listOf()
) : RecipeFiltersDefault(), CollectionDescription

@Serializable data class FilteredUserListRoute(
    override val title: String,
    override val sortingOptionId: Long?
) : UserFiltersDefault(), CollectionDescription

fun NavController.navigateToFilteredRecipeList(
    title: String,
    sortingOptionId: Long? = null,
    userIds: List<Long> = listOf(),
    categoryIds: List<Long> = listOf(),
    lifeStyleIds: List<Long> = listOf(),
    navOptions: NavOptions? = null
) = navigate(route = FilteredRecipeListRoute(
    title,
    sortingOptionId,
    userIds,
    categoryIds,
    lifeStyleIds
), navOptions)

fun NavController.navigateToFilteredUserList(
    title: String,
    sortingOptionId: Long? = null,
    navOptions: NavOptions? = null
) = navigate(route = FilteredUserListRoute(
    title,
    sortingOptionId
), navOptions)

fun NavController.navigateToRecipeDetail(recipeId: Long? = null, collectionIndex: Int? = null, moderation: Boolean = false, navOptions: NavOptions? = null) =
    navigate(route = RecipeDetailRoute(recipeId, collectionIndex, moderation), navOptions)

fun NavController.navigateToUserDetail(userId: Long, collectionIndex: Int? = null, navOptions: NavOptions? = null) =
    navigate(route = UserDetailRoute(userId, collectionIndex), navOptions)

fun NavGraphBuilder.catalogueNavigation(
    navController: NavHostController
) {
    navigation<Catalogue>(startDestination = FeedRoute) {
        composableWithCompositionLocal<FeedRoute> {
            Feed(
                collections = allCollections,
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
                title = filteredRecipeListRoute.title,
                recipes = RecipeService.recipesFiltered(filteredRecipeListRoute),
                onRecipeClick = { recipe ->
                    navController.navigateToRecipeDetail(recipe.id)
                },
                onFavoriteClick = { _ -> },
                isLoggedIn = true
            )
        }
        composableWithCompositionLocal<FilteredUserListRoute> { backStackEntry ->
            val filteredUserListRoute: FilteredUserListRoute = backStackEntry.toRoute()

            UserListPage(
                title = filteredUserListRoute.title,
                users = RecipeService.usersFiltered(filteredUserListRoute),
                onUserClick = { user -> navController.navigateToUserDetail(user.id) }
            )
        }
        composableWithCompositionLocal<RecipeDetailRoute> { backStackEntry ->
            val recipeDetailsRoute: RecipeDetailRoute = backStackEntry.toRoute()
            var isInEditMode by remember { mutableStateOf(recipeDetailsRoute.recipeId == null || recipeDetailsRoute.moderation) }

            val recipe = RecipeService.allRecipes.firstOrNull { it.id == recipeDetailsRoute.recipeId } ?: Recipe(0, "Новый рецепт", 0, 0f, "", false, false, RecipeService.currentUser, listOf(), listOf(), listOf())

            if (recipe.isPremium && !RecipeService.currentUser.hasPremium) {
                PremiumAccessRequiredScreen(
                    onGoBack = { navController.navigateUp() }
                )
            } else {
                RecipeDetail(
                    recipe = recipe,
                    collectionIndex = recipeDetailsRoute.collectionIndex,
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
                collectionIndex = userDetailsRoute.collectionIndex,
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

private val allCollections = listOf(
    RecipeCollection(
        route = FilteredRecipeListRoute(title = "Завтраки", sortingOptionId = 1),
    ),
    UserCollection(
        route = FilteredUserListRoute(title = "Повары JustCook", sortingOptionId = 1)
    ),
    RecipeCollection(
        route = FilteredRecipeListRoute(title = "Простые рецепты", sortingOptionId = 2)
    )
)