package com.example.favorite.favorite

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.components.CustomizableItemList
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RecipeItem
import com.example.components.RequireSignInPage
import com.example.favorite.R
import kotlin.math.roundToInt
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.short_models.RecipeShort
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService

@Composable
fun FavoritePage(
    onRecipeClick: (RecipeShort) -> Unit
) {
    val recipeService = LocalRecipeService.current!!
    val tokenService = LocalTokenService.current!!
    val context = LocalContext.current

    var isSignedIn by remember { mutableStateOf(tokenService.isSighedIn()) }

    RequireSignInPage(
        isSignedIn = isSignedIn,
        onSignIn = { isSignedIn = true }
    ) {
        Favorite(
            recipeService = recipeService,
            onError = { Toast.makeText(context, context.resources.getString(R.string.favorite_error), Toast.LENGTH_LONG).show() },
            onRecipeClick = onRecipeClick
        )
    }
}

@Composable
fun Favorite(
    recipeService: RecipeService,
    onRecipeClick: (RecipeShort) -> Unit,
    onError: (e: Throwable) -> Unit,
    modifier: Modifier = Modifier,
    favoriteViewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModel.provideFactory(
        recipeService = recipeService
    ))
) {
    val itemAnimationSpecFade = nonSpatialExpressiveSpring<Float>()
    val itemPlacementSpec = spatialExpressiveSpring<IntOffset>()
    val uiState by favoriteViewModel.favoriteUiState.collectAsState()
    val lazyPagingItems = uiState.dataFlow.collectAsLazyPagingItems()

    LaunchedEffect(true) {
        favoriteViewModel.onError = onError
        favoriteViewModel.refresh()
    }

    JustSurface(modifier = modifier.fillMaxSize()) {
        Column {
            PageTitle(stringResource(R.string.feature_header))
            CustomizableItemList(
                removedItems = uiState.removedItems,
                lazyPagingItems = lazyPagingItems,
                customItem = { recipe ->
                    SwipeDismissItem(
                        onDismiss = { favoriteViewModel.onRemoveRecipe(recipe) },
                        modifier = Modifier.animateItem(
                            fadeInSpec = itemAnimationSpecFade,
                            fadeOutSpec = itemAnimationSpecFade,
                            placementSpec = itemPlacementSpec
                        ),
                        background = { progress ->
                            SwipeDismissItemBackground(progress)
                        },
                    ) {
                        RecipeItem(
                            recipe = recipe,
                            onRecipeClick = onRecipeClick,
                            onIconClick = favoriteViewModel::onRemoveRecipe,
                            iconPainter = rememberVectorPainter(Icons.Filled.Close)
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun SwipeDismissItemBackground(progress: Float) {
    Column(
        modifier = Modifier
            .background(JustCookColorPalette.colors.uiBackground)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        // Set 4.dp padding only if progress is less than halfway
        val padding: Dp by animateDpAsState(
            if (progress < 0.5f) 4.dp else 0.dp, label = "padding"
        )
        BoxWithConstraints(
            Modifier
                .fillMaxWidth(progress)
        ) {
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .height(maxWidth)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(percent = ((1 - progress) * 100).roundToInt()),
                color = JustCookColorPalette.colors.error
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Icon must be visible while in this width range
                    if (progress in 0.125f..0.475f) {
                        // Icon alpha decreases as it is about to disappear
                        val iconAlpha: Float by animateFloatAsState(
                            if (progress > 0.4f) 0.5f else 1f, label = "icon alpha"
                        )

                        Icon(
                            imageVector = Icons.Filled.Delete,
                            modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer(alpha = iconAlpha),
                            tint = JustCookColorPalette.colors.uiBackground,
                            contentDescription = null,
                        )
                    }
                    /*Text opacity increases as the text is supposed to appear in
                                    the screen*/
                    val textAlpha by animateFloatAsState(
                        if (progress > 0.5f) 1f else 0.5f, label = "text alpha"
                    )
                    if (progress > 0.5f) {
                        Text(
                            text = stringResource(id = R.string.remove_item),
                            style = MaterialTheme.typography.titleMedium,
                            color = JustCookColorPalette.colors.uiBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .graphicsLayer(
                                    alpha = textAlpha
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SwipeDismissItem(
    modifier: Modifier = Modifier,
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
    onDismiss: () -> Unit,
    background: @Composable (progress: Float) -> Unit,
    content: @Composable (isDismissed: Boolean) -> Unit,
) {
    // Hold the current state from the Swipe to Dismiss composable
    val dismissState = rememberSwipeToDismissBoxState()
    val isDismissed = dismissState.currentValue == SwipeToDismissBoxValue.EndToStart

    LaunchedEffect(isDismissed) {
        if (isDismissed) {
            onDismiss()
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = true,
        enter = enter,
        exit = exit
    ) {
        SwipeToDismissBox(
            modifier = modifier,
            state = dismissState,
            enableDismissFromStartToEnd = false,
            backgroundContent = { background(dismissState.progress) },
            content = { content(isDismissed) }
        )
    }
}