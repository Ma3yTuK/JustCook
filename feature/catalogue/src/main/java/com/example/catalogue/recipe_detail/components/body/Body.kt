package com.example.catalogue.recipe_detail.components.body

import android.net.Uri
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.BottomBarHeight
import com.example.catalogue.recipe_detail.GradientScroll
import com.example.catalogue.recipe_detail.HzPadding
import com.example.catalogue.recipe_detail.ImageOverlap
import com.example.catalogue.recipe_detail.MinTitleOffset
import com.example.catalogue.recipe_detail.TitleHeight
import com.example.catalogue.recipe_detail.components.body.components.Author
import com.example.catalogue.recipe_detail.components.body.components.RecipeWeight
import com.example.catalogue.recipe_detail.components.body.components.description.Description
import com.example.catalogue.recipe_detail.components.body.components.ingredients.Ingredients
import com.example.catalogue.recipe_detail.components.body.components.reviews.Reviews
import com.example.catalogue.recipe_detail.components.body.components.reviews.components.ReviewItem
import com.example.catalogue.recipe_detail.components.body.components.steps.Steps
import com.example.components.CustomizableItemList
import com.example.components.JustDivider
import com.example.components.JustSurface
import com.example.components.LocalSharedTransitionScope
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.IngredientUnitConversion
import com.example.data.models.PageData
import com.example.data.models.Recipe
import com.example.data.models.RecipeStep
import com.example.data.models.Review
import com.example.data.models.User
import com.example.data.models.short_models.UserShort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Body(
    recipe: Recipe,
    ingredientQuery: String,
    onIngredientQueryChange: (String) -> Unit,
    reviews: Flow<PagingData<Review>>,
    canDeleteReview: (Review) -> Boolean,
    onDeleteReview: (Long) -> Unit,
    onSubmitReview: (rating: Long, comment: String) -> Unit,
    onUserClick: (UserShort) -> Unit,
    onChangeDescription: (String) -> Unit,
    updateConversionsForIngredient: (Long) -> Unit,
    conversionsForIngredient: List<IngredientUnitConversion>?,
    updateIngredient: (Int, IngredientUnitConversion?, Long?) -> Unit,
    onDeleteIngredient: (Int) -> Unit,
    updateStep: (Int, String?, Uri?) -> Unit,
    onDeleteStep: (Int) -> Unit,
    addStep: (String, Uri?) -> Unit,
    addIngredient: (IngredientUnitConversion, Long) -> Unit,
    allIngredients: List<Ingredient>,
    isLoggedIn: Boolean,
    onWeightChange: (Long) -> Unit,
    isInEditMode: Boolean,
    scroll: LazyListState
) {
    val lazyPagingItems = reviews.collectAsLazyPagingItems()

    val sharedTransitionScope =
        LocalSharedTransitionScope.current ?: throw IllegalStateException("No scope found")
    with(sharedTransitionScope) {
        Column(modifier = Modifier.skipToLookaheadSize()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(MinTitleOffset)
            )

            LazyColumn (
                state = scroll,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(Modifier.height(GradientScroll))
                    Spacer(Modifier.height(ImageOverlap))
                    JustSurface(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        Column {
                            Spacer(Modifier.height(TitleHeight))
                            RecipeWeight(recipe, isInEditMode, onWeightChange)
                            Spacer(Modifier.height(15.dp))
                            Author(recipe.user, onClick = { onUserClick(recipe.user) })
                            Spacer(Modifier.height(25.dp))
                            Description(recipe, onChangeDescription, isInEditMode)

                            Spacer(Modifier.height(40.dp))
                            Ingredients(recipe.ingredients, ingredientQuery, onIngredientQueryChange, updateConversionsForIngredient, updateIngredient, onDeleteIngredient, addIngredient, allIngredients, conversionsForIngredient, isInEditMode)

                            Spacer(Modifier.height(16.dp))
                            Steps(recipe.steps, updateStep, onDeleteStep, addStep, isInEditMode)

                            if (!isInEditMode) {
                                Spacer(Modifier.height(32.dp))
                                Reviews(onSubmitReview, isLoggedIn)
                            }
                        }
                    }
                }
                if (!isInEditMode) {
                    if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                        item {
                            Text(
                                text = stringResource(com.example.components.R.string.loading),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    if (lazyPagingItems.itemCount == 0 && lazyPagingItems.loadState.isIdle || lazyPagingItems.loadState.hasError) {
                        item(key = "nothing") {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(com.example.components.R.string.no_recipes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = JustCookColorPalette.colors.textHelp
                                )
                            }
                        }
                    } else {
                        items(
                            lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey { it.id }
                        ) { index ->
                            val review = lazyPagingItems[index]
                            if (review != null) {
                                ReviewItem(review, canDeleteReview, onDeleteReview)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
                item {
                    if (isLoggedIn) {
                        Spacer(
                            modifier = Modifier
                                .padding(bottom = BottomBarHeight)
                                .navigationBarsPadding()
                                .height(8.dp)
                        )
                    }
                }
            }
        }
    }
}