package com.example.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import com.example.components.theme.JustCookColorPalette
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.data.models.Recipe
import androidx.compose.foundation.lazy.items

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onItemClick: (Recipe) -> Unit,
    onSaveClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(recipes, key = { it.id }) { recipe ->
            ListItem(recipe, onItemClick, onSaveClick)
        }
    }
}

@Composable
fun ListItem(
    recipe: Recipe,
    onItemClick: (Recipe) -> Unit,
    onSaveClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(recipe) }
//            .background(JetsnackTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)

    ) {
        val (image, name, tag, priceSpacer, price, remove) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        RecipeImage(
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
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
//        IconButton(
//            onClick = { removeSnack(snack.id) },
//            modifier = Modifier
//                .constrainAs(remove) {
//                    top.linkTo(parent.top)
//                    end.linkTo(parent.end)
//                }
//                .padding(top = 12.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Close,
//                tint = JetsnackTheme.colors.iconSecondary,
//                contentDescription = stringResource(R.string.label_remove)
//            )
//        }
        Text(
            text = "Tag",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.constrainAs(tag) {
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
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )
//        Text(
//            text = formatPrice(snack.price),
//            style = MaterialTheme.typography.titleMedium,
//            color = JetsnackTheme.colors.textPrimary,
//            modifier = Modifier.constrainAs(price) {
//                linkTo(
//                    start = image.end,
//                    end = quantity.start,
//                    startMargin = 16.dp,
//                    endMargin = 16.dp,
//                    bias = 0f
//                )
//            }
//        )
        IconButton(
            onClick = { onSaveClick(recipe) },
            modifier = Modifier
                .constrainAs(remove) {
                    baseline.linkTo(tag.baseline)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = if (recipe.isFavourite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                contentDescription = stringResource(R.string.label_save)
            )
        }
    }
}