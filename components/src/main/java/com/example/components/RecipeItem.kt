package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.components.entity_collection_view.components.recipe_item.components.card_contentent.components.AMOUNT_OF_GRAM_FOR_CALORIES
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe
import com.example.data.models.short_models.RecipeShort

@Composable
fun RecipeItem(
    recipe: RecipeShort,
    onRecipeClick: (RecipeShort) -> Unit,
    onIconClick: (RecipeShort) -> Unit,
    iconPainter: Painter?,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRecipeClick(recipe) }
            .background(JustCookColorPalette.colors.uiBackground)
            .padding(horizontal = 24.dp)

    ) {
        val (divider, image, name, calories, starSpacer, stars, icon) = createRefs()
        createVerticalChain(name, calories, stars, chainStyle = ChainStyle.Packed)
        JustImage(
            image = recipe.image,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = recipe.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = if (recipe.isVerified) JustCookColorPalette.colors.textSecondary else JustCookColorPalette.colors.error,
            modifier = Modifier.constrainAs(name) {
                width = Dimension.preferredWrapContent
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = if (iconPainter == null) parent.end else icon.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        iconPainter?.let {
            IconButton(
                onClick = { onIconClick(recipe) },
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 12.dp)
            ) {
                Icon(
                    painter = it,
                    tint = JustCookColorPalette.colors.iconSecondary,
                    contentDescription = stringResource(R.string.label_remove)
                )
            }
        }
        Text(
            text = "${if (recipe.weight != 0L) recipe.calories * AMOUNT_OF_GRAM_FOR_CALORIES / recipe.weight else 0} ${stringResource(R.string.calories)}",
            style = MaterialTheme.typography.bodyLarge,
            color = JustCookColorPalette.colors.textHelp,
            modifier = Modifier.constrainAs(calories) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Spacer(
            Modifier
                .height(8.dp)
                .constrainAs(starSpacer) {
                    linkTo(top = calories.bottom, bottom = stars.top)
                }
        )
        RecipeStars(
            recipeRating = recipe.rating,
            recipeIsPremium = recipe.isPremium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(stars) {
                    linkTo(
                        start = image.end,
                        end = parent.end,
                        startMargin = 16.dp,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
        )
        JustDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}