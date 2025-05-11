package com.example.catalogue.recipe_detail.components.body

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catalogue.recipe_detail.BottomBarHeight
import com.example.catalogue.recipe_detail.GradientScroll
import com.example.catalogue.recipe_detail.ImageOverlap
import com.example.catalogue.recipe_detail.MinTitleOffset
import com.example.catalogue.recipe_detail.TitleHeight
import com.example.catalogue.recipe_detail.components.body.components.description.Description
import com.example.catalogue.recipe_detail.components.body.components.ingredients.Ingredients
import com.example.catalogue.recipe_detail.components.body.components.reviews.Reviews
import com.example.catalogue.recipe_detail.components.body.components.steps.Steps
import com.example.components.JustDivider
import com.example.components.JustSurface
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Ingredient
import com.example.data.models.IngredientIngredientConversion
import com.example.data.models.Recipe
import com.example.data.models.RecipeIngredient
import com.example.data.models.Review

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Body(
    recipe: Recipe,
    canDeleteReview: (Review) -> Boolean,
    onDeleteReview: (Long) -> Unit,
    onSubmitReview: (rating: Float, comment: String) -> Unit,
    onChangeDescription: (String) -> Unit,
    getConversionsForIngredient: (Ingredient) -> List<IngredientIngredientConversion>,
    setIngredientConversion: (RecipeIngredient, IngredientIngredientConversion?) -> Unit,
    setIngredientAmount: (RecipeIngredient, Float) -> Unit,
    onDeleteIngredient: (RecipeIngredient) -> Unit,
    addIngredient: (Ingredient, Float, IngredientIngredientConversion?) -> Unit,
    allIngredients: List<Ingredient>,
    isLoggedIn: Boolean,
    isInEditMode: Boolean,
    scroll: ScrollState
) {
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

            Column(
                modifier = Modifier.verticalScroll(scroll)
            ) {
                Spacer(Modifier.height(GradientScroll))
                Spacer(Modifier.height(ImageOverlap))
                JustSurface(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    Column {
                        Spacer(Modifier.height(TitleHeight))
                        Description(recipe, onChangeDescription, isInEditMode)

                        Spacer(Modifier.height(40.dp))
                        Ingredients(recipe.ingredients, getConversionsForIngredient, setIngredientConversion, setIngredientAmount, onDeleteIngredient, addIngredient, allIngredients, isInEditMode)

                        Spacer(Modifier.height(16.dp))
                        Steps(recipe.steps)

                        Spacer(Modifier.height(32.dp))
                        Reviews(recipe.reviews, canDeleteReview, onDeleteReview, onSubmitReview, isLoggedIn)

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