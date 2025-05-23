package com.example.catalogue.feed

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.JustDivider
import com.example.components.JustSurface
import com.example.components.entity_collection_view.EntityCollectionView
import com.example.catalogue.collections.EntityCollection
import com.example.components.ErrorPage
import com.example.components.RequireSignInPage
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.data.repositories.LocalImageRepository
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.user.LocalUserService
import com.example.catalogue.R
import com.example.components.LoadingOverlay
import com.example.data.services.recipe.RecipeService
import com.example.data.services.user.UserService

@Composable
fun FeedPage(
    onRecipeClick: (RecipeShort, Int) -> Unit,
    onUserClick: (UserShort, Int) -> Unit,
    onCollectionClick: (EntityCollection) -> Unit,
) {
    val userService = LocalUserService.current!!
    val recipeService = LocalRecipeService.current!!
    val tokenService = LocalTokenService.current!!

    var isSignedIn by remember { mutableStateOf(tokenService.isSighedIn()) }
    var isError by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    RequireSignInPage(
        isSignedIn = isSignedIn,
        onSignIn = { isSignedIn = true }
    ) {
        if (isError) {
            ErrorPage(stringResource(R.string.default_feed_error_message))
        } else {
            Feed(
                userService = userService,
                recipeService = recipeService,
                onError = { isError = true; error = it.message ?: "noting" },
                onRecipeClick = onRecipeClick,
                onUserClick = onUserClick,
                onCollectionClick = onCollectionClick
            )
        }
    }
}

@Composable
fun Feed(
    userService: UserService,
    recipeService: RecipeService,
    onError: (e: Throwable) -> Unit,
    onRecipeClick: (RecipeShort, Int) -> Unit,
    onUserClick: (UserShort, Int) -> Unit,
    onCollectionClick: (EntityCollection) -> Unit,
    modifier: Modifier = Modifier,
    feedViewModel: FeedViewModel = viewModel(factory = FeedViewModel.provideFactory(
        userService = userService,
        recipeService = recipeService
    ))
) {
    val uiState by feedViewModel.feedUiState.collectAsState()
    LaunchedEffect(true) {
        feedViewModel.onError = onError
        feedViewModel.refresh()
    }
    JustSurface(modifier = modifier.fillMaxSize()) {
        LoadingOverlay(isLoading = feedViewModel.isLoading) {
            LazyColumn(modifier = modifier) {
                itemsIndexed(uiState.entityCollections) { index, collection ->
                    if (index > 0) {
                        JustDivider(thickness = 2.dp)
                    }

                    if (collection.entities.isNotEmpty()) {
                        EntityCollectionView(
                            collectionName = collection.route.title,
                            collectionIndex = index,
                            entityCollection = collection.entities,
                            onCollectionClick = { onCollectionClick(collection) },
                            onRecipeClick = onRecipeClick,
                            onUserClick = onUserClick
                        )
                    }
                }
            }
        }
    }
}