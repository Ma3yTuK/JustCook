/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetsnack.ui.home.cart

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.CustomizableRecipeList
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RecipeItem
import com.example.data.models.Recipe
import com.example.favorite.R
import kotlin.math.roundToInt
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette

@Composable
fun Feature(
    favoriteRecipes: List<Recipe>,
    onRemoveRecipe: (Recipe) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemAnimationSpecFade = nonSpatialExpressiveSpring<Float>()
    val itemPlacementSpec = spatialExpressiveSpring<IntOffset>()

    JustSurface(modifier = modifier.fillMaxSize()) {
        Column {
            PageTitle(stringResource(R.string.feature_header))
            CustomizableRecipeList(
                recipes = favoriteRecipes,
                customItem = { recipe ->
                    SwipeDismissItem(
                        onDismiss = { onRemoveRecipe(recipe) },
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
                            onIconClick = onRemoveRecipe,
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
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { onDismiss(); true }
    )
    // Boolean value used for hiding the item if the current state is dismissed
    val isDismissed = dismissState.currentValue == SwipeToDismissBoxValue.EndToStart

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