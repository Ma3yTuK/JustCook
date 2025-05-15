package com.example.catalogue.recipe_detail

import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.Body
import com.example.catalogue.recipe_detail.components.Header
import com.example.catalogue.recipe_detail.components.image.Image
import com.example.catalogue.recipe_detail.components.RecipeBottomBar
import com.example.catalogue.recipe_detail.components.title.Title
import com.example.components.OnTopButton
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.IngredientIngredientConversion
import com.example.data.models.RecipeIngredient
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User

val BottomBarHeight = 40.dp
val TitleHeight = 128.dp
val GradientScroll = 180.dp
val ImageOverlap = 115.dp
val MinTitleOffset = 56.dp
val MinImageOffset = 12.dp
val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
val ExpandedImageSize = 300.dp
val CollapsedImageSize = 150.dp
val HzPadding = Modifier.padding(horizontal = 24.dp)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeDetail(
    recipe: Recipe,
    collectionId: Long,
    canDeleteReview: (Review) -> Boolean,
    onDeleteReview: (Long) -> Unit,
    onSubmitReview: (rating: Float, comment: String) -> Unit,
    onUserClick: (User) -> Unit,
    onNameChange: (String) -> Unit,
    onChangeRecipeImage: (Uri) -> Unit,
    onChangeDescription: (String) -> Unit,
    getConversionsForIngredient: (Ingredient) -> List<IngredientIngredientConversion>,
    setIngredientConversion: (RecipeIngredient, IngredientIngredientConversion?) -> Unit,
    setIngredientAmount: (RecipeIngredient, Float) -> Unit,
    onDeleteIngredient: (RecipeIngredient) -> Unit,
    setStepDescription: (RecipeStep, String) -> Unit,
    setStepImage: (RecipeStep, Uri) -> Unit,
    onDeleteStep: (RecipeStep) -> Unit,
    addStep: (String) -> Unit,
    addIngredient: (Ingredient, Float, IngredientIngredientConversion?) -> Unit,
    allIngredients: List<Ingredient>,
    isLoggedIn: Boolean,
    isInEditMode: Boolean,
    canEdit: Boolean,
    upPress: () -> Unit,
    onToggleFavoritePress: () -> Unit,
    onEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
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
        Box(
            modifier
                .clip(RoundedCornerShape(roundedCornerAnim))
                .sharedBounds(
                    rememberSharedContentState(
                        key = RecipeSharedElementKey(
                            recipeId = recipe.id,
                            type = RecipeSharedElementType.Bounds,
                            collectionId = collectionId
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
            val scroll = rememberScrollState(0)

            Header(recipe, collectionId)
            Body(recipe, canDeleteReview, onDeleteReview, onSubmitReview, onUserClick, onChangeDescription, getConversionsForIngredient, setIngredientConversion, setIngredientAmount, onDeleteIngredient, setStepDescription, setStepImage, onDeleteStep, addStep, addIngredient, allIngredients, isLoggedIn, isInEditMode, scroll)
            Title(recipe, collectionId, isInEditMode, onNameChange) { scroll.value }
            Image(recipe.id, collectionId, isInEditMode, onChangeRecipeImage) { scroll.value }
            OnTopButton(
                onPress = upPress,
                painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ArrowBack),
                contentDescription = stringResource(R.string.label_back)
            )

            if (isLoggedIn) {
                if (canEdit) {
                    if (isInEditMode) {
                        OnTopButton(
                            onPress = onCancelEdit,
                            painter = rememberVectorPainter(Icons.Default.Clear),
                            contentDescription = stringResource(R.string.cancel_edit),
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    } else {
                        OnTopButton(
                            onPress = onEdit,
                            painter = rememberVectorPainter(Icons.Default.Edit),
                            contentDescription = stringResource(R.string.edit),
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
                RecipeBottomBar(recipe, onToggleFavoritePress, isInEditMode, onSaveEdit, onCancelEdit, isValid, modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}