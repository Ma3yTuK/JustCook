package com.example.catalogue.recipe_detail.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.BottomBarHeight
import com.example.catalogue.recipe_detail.HzPadding
import com.example.components.JustButton
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeBottomBar(
    recipe: Recipe,
    onToggleFavouritePress: () -> Unit,
    isInEditMode: Boolean,
    onSaveRecipe: () -> Unit,
    onRemoveRecipe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current ?: throw IllegalStateException("No Shared scope")
    val animatedVisibilityScope =
        LocalNavAnimatedVisibilityScope.current ?: throw IllegalStateException("No Shared scope")
    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Box(
                modifier = modifier
                    .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 4f)
                    .animateEnterExit(
                        enter = slideInVertically(
                            tween(
                                300,
                                delayMillis = 300
                            )
                        ) { it } + fadeIn(tween(300, delayMillis = 300)),
                        exit = slideOutVertically(tween(50)) { it } +
                                fadeOut(tween(50))
                    )
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .then(HzPadding)
                            .heightIn(min = BottomBarHeight)
                    ) {
                        if (isInEditMode) {
                            JustButton(
                                onClick = onSaveRecipe,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.save_recipe),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                            JustButton(
                                onClick = onRemoveRecipe,
                                backgroundGradient = JustCookColorPalette.colors.gradient2_3,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.remove_recipe),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }

                        } else {
                            JustButton(
                                onClick = onToggleFavouritePress,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (recipe.isFavourite) stringResource(R.string.remove_from_favourite) else stringResource(R.string.add_to_favourite),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}