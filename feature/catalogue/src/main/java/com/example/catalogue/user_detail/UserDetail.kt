package com.example.catalogue.user_detail

import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.components.JustImage
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.User
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import com.example.catalogue.R
import com.example.components.ErrorPage
import com.example.components.JustDivider
import com.example.components.JustSurface
import com.example.components.LoadingOverlay
import com.example.components.OnTopButton
import com.example.components.RecipeList
import com.example.components.RequireSignInPage
import com.example.components.entity_collection_view.components.UserSharedElementKey
import com.example.components.entity_collection_view.components.UserSharedElementType
import com.example.components.entity_collection_view.components.userDetailBoundsTransform
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService
import com.example.data.services.user.LocalUserService
import com.example.data.services.user.UserService
import kotlinx.coroutines.flow.Flow

@Composable
fun UserDetailPage(
    userId: Long,
    collectionIndex: Int?,
    onRecipeClick: (RecipeShort) -> Unit,
    upPress: () -> Unit
) {
    val userService = LocalUserService.current!!
    val recipeService = LocalRecipeService.current!!
    val tokenService = LocalTokenService.current!!

    var isSeriousError by remember { mutableStateOf(false) }

    if (isSeriousError) {
        ErrorPage(stringResource(R.string.default_feed_error_message))
    } else {
        UserDetail(
            userService = userService,
            recipeService = recipeService,
            userId = userId,
            onError = { isSeriousError = !isSeriousError },
            collectionIndex = collectionIndex,
            onRecipeClick = onRecipeClick,
            upPress = upPress,
            isLoggedIn = tokenService.isSighedIn()
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserDetail(
    userService: UserService,
    recipeService: RecipeService,
    userId: Long,
    onError: (e: Throwable) -> Unit,
    collectionIndex: Int?,
    onRecipeClick: (RecipeShort) -> Unit,
    upPress: () -> Unit,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier,
    viewModel: UserDetailViewModel = viewModel(factory = UserDetailViewModel.provideFactory(
        userService = userService,
        recipeService = recipeService
    ))
) {
    val uiState by viewModel.userDetailUiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.onError = onError
        viewModel.refresh(userId)
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")

    with(sharedTransitionScope) {
        JustSurface(Modifier.fillMaxSize()) {
            LoadingOverlay(uiState.isLoading) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    JustImage(
                        image = uiState.user.image,
                        contentDescription = null,
                        isVerified = uiState.user.isVerified,
                        modifier = Modifier
                            .size(150.dp)
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = UserSharedElementKey(
                                        userId = uiState.user.id,
                                        type = UserSharedElementType.Image,
                                        collectionIndex = collectionIndex
                                    )
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                exit = fadeOut(),
                                enter = fadeIn(),
                                boundsTransform = userDetailBoundsTransform
                            ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = uiState.user.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = JustCookColorPalette.colors.textPrimary,
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState(
                                key = UserSharedElementKey(
                                    userId = uiState.user.id,
                                    type = UserSharedElementType.Title,
                                    collectionIndex = collectionIndex
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = userDetailBoundsTransform
                        )
                            .widthIn(max = 300.dp)
                    )
                    Text(
                        text = if (uiState.user.isVerified) stringResource(R.string.status_verified) else stringResource(
                            R.string.status_not_verified
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = JustCookColorPalette.colors.textHelp,
                        modifier = Modifier
                            .wrapContentWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    JustDivider()

                    RecipeList(uiState.dataFlow, onRecipeClick, viewModel::onFavoriteClick, isLoggedIn, updatedItems = uiState.savedItems)
                }
                OnTopButton(
                    onPress = upPress,
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ArrowBack),
                    contentDescription = stringResource(R.string.label_back)
                )
            }
        }
    }
}