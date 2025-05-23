package com.example.catalogue.recipe_detail

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.data.models.Recipe
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.Body
import com.example.catalogue.recipe_detail.components.Header
import com.example.catalogue.recipe_detail.components.image.Image
import com.example.catalogue.recipe_detail.components.RecipeBottomBar
import com.example.catalogue.recipe_detail.components.title.Title
import com.example.components.ErrorPage
import com.example.components.LoadingOverlay
import com.example.components.OnTopButton
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User
import com.example.data.models.short_models.UserShort
import com.example.data.repositories.ImageRepository
import com.example.data.repositories.LocalImageRepository
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.auth.TokenService
import com.example.data.services.ingredient.IngredientService
import com.example.data.services.ingredient.LocalIngredientService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.recipe.RecipeService
import com.example.data.services.review.LocalReviewService
import com.example.data.services.review.ReviewService
import com.example.data.services.user.LocalUserService
import com.example.data.services.user.UserService

val BottomBarHeight = 40.dp
val TitleHeight = 110.dp
val GradientScroll = 180.dp
val ImageOverlap = 115.dp
val MinTitleOffset = 56.dp
val MinImageOffset = 12.dp
val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
val ExpandedImageSize = 300.dp
val CollapsedImageSize = 150.dp
val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun RecipeDetailPage(
    recipeId: Long?,
    collectionIndex: Int?,
    onUserClick: (UserShort) -> Unit,
    upPress: () -> Unit,
) {
    val recipeService = LocalRecipeService.current!!
    val userService = LocalUserService.current!!
    val reviewService = LocalReviewService.current!!
    val imageRepository = LocalImageRepository.current!!
    val tokenService = LocalTokenService.current!!
    val ingredientService = LocalIngredientService.current!!
    val context = LocalContext.current

    var isSeriousError by remember { mutableStateOf(false) }

    if (isSeriousError) {
        ErrorPage(stringResource(R.string.default_feed_error_message))
    } else {
        RecipeDetail(
            recipeService = recipeService,
            reviewService = reviewService,
            userService = userService,
            ingredientService = ingredientService,
            imageRepository = imageRepository,
            tokenService = tokenService,
            recipeId = recipeId,
            collectionIndex = collectionIndex,
            upPress = upPress,
            onUserClick = onUserClick,
            onError = { Toast.makeText(context, context.resources.getString(R.string.default_feed_error_message), Toast.LENGTH_LONG).show() },
            onSeriousError = { isSeriousError = true }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeDetail(
    recipeService: RecipeService,
    userService: UserService,
    reviewService: ReviewService,
    ingredientService: IngredientService,
    onUserClick: (UserShort) -> Unit,
    imageRepository: ImageRepository,
    tokenService: TokenService,
    onError: (e: Throwable) -> Unit,
    onSeriousError: (e: Throwable) -> Unit,
    recipeId: Long?,
    collectionIndex: Int?,
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailViewModel = viewModel(factory = RecipeDetailViewModel.provideFactory(
        recipeService = recipeService,
        userService = userService,
        reviewService = reviewService,
        imageRepository = imageRepository,
        tokenService = tokenService,
        ingredientService = ingredientService
    ))
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.onDelete = upPress
        viewModel.onError = onError
        viewModel.onSeriousError = onSeriousError
        viewModel.refresh(recipeId)
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")
    val roundedCornerAnim by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> 20.dp
                EnterExitState.Visible -> 0.dp
                EnterExitState.PostExit -> 20.dp
            }
        }

    with(sharedTransitionScope) {
        LoadingOverlay(uiState.isLoading) {
            Box(
                modifier
                    .clip(RoundedCornerShape(roundedCornerAnim))
                    .sharedBounds(
                        rememberSharedContentState(
                            key = RecipeSharedElementKey(
                                recipeId = uiState.recipe.id,
                                type = RecipeSharedElementType.Bounds,
                                collectionIndex = collectionIndex
                            )
                        ),
                        animatedVisibilityScope,
                        clipInOverlayDuringTransition =
                            OverlayClip(RoundedCornerShape(roundedCornerAnim)),
                        boundsTransform = recipeDetailBoundsTransform,
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                    )
                    .fillMaxSize()
                    .background(color = JustCookColorPalette.colors.uiBackground)
            ) {
                val scroll = rememberLazyListState(0)

                Header(uiState.recipe, collectionIndex)
                Body(
                    recipe = uiState.recipe,
                    ingredientQuery =  uiState.ingredientQuery,
                    onIngredientQueryChange = viewModel::onIngredientQueryChange,
                    reviews = uiState.reviewFlow,
                    canDeleteReview = viewModel::canDeleteReview,
                    onDeleteReview = viewModel::onDeleteReview,
                    onSubmitReview = viewModel::onSubmitReview,
                    onUserClick = onUserClick,
                    onChangeDescription = viewModel::onDescriptionChange,
                    updateConversionsForIngredient = viewModel::updateConversionsForIngredient,
                    conversionsForIngredient = uiState.conversions,
                    updateIngredient = viewModel::updateIngredient,
                    onDeleteIngredient = viewModel::deleteIngredient,
                    viewModel::updateStep,
                    viewModel::deleteStep,
                    viewModel::addStep,
                    viewModel::addIngredient,
                    uiState.allIngredients,
                    uiState.isLoggedIn,
                    viewModel::onWeightChange,
                    uiState.isInEditMode,
                    scroll
                )
                Title(uiState.recipe, collectionIndex, uiState.isInEditMode, viewModel::onNameChange) { scroll.firstVisibleItemScrollOffset }
                Image(uiState.recipe, collectionIndex, uiState.isInEditMode, viewModel::onImageChange) { scroll.firstVisibleItemScrollOffset }
                OnTopButton(
                    onPress = upPress,
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ArrowBack),
                    contentDescription = stringResource(R.string.label_back)
                )

                if (uiState.isLoggedIn) {
                    if (uiState.canEdit) {
                        if (uiState.isInEditMode) {
                            if (!uiState.isModerating && !uiState.isNewRecipe) {
                                OnTopButton(
                                    onPress = viewModel::onCancelEdit,
                                    painter = rememberVectorPainter(Icons.Default.Clear),
                                    contentDescription = stringResource(R.string.cancel_edit),
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )
                            }
                        } else {
                            OnTopButton(
                                onPress = viewModel::onEdit,
                                painter = rememberVectorPainter(Icons.Default.Edit),
                                contentDescription = stringResource(R.string.edit),
                                modifier = Modifier.align(Alignment.TopEnd)
                            )
                        }
                    }
                    RecipeBottomBar(
                        uiState.recipe,
                        viewModel::onToggleFavoriteClick,
                        uiState.isInEditMode,
                        viewModel::onSaveEdit,
                        viewModel::onRemoveRecipe,
                        uiState.isValid,
                        uiState.isNewRecipe,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}