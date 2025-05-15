package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onFavoriteClick: (Recipe) -> Unit,
    showIcon: Boolean,
    modifier: Modifier = Modifier
) {
    CustomizableRecipeList(
        recipes = recipes,
        customItem = { recipe ->
            val recipeIcon = if (showIcon) rememberVectorPainter(
                if (recipe.isFavorite)
                    Icons.Default.Favorite
                else
                    Icons.Default.FavoriteBorder
            ) else null
            RecipeItem(
                recipe = recipe,
                onRecipeClick = onRecipeClick,
                onIconClick = onFavoriteClick,
                iconPainter = recipeIcon
            )
        },
        modifier = modifier
    )
}

@Composable
fun CustomizableRecipeList(
    recipes: List<Recipe>,
    customItem: @Composable LazyItemScope.(Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        if (recipes.isEmpty()) {
            item(key = "nothing") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_recipes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = JustCookColorPalette.colors.textHelp
                    )
                }
            }
        } else {
            items(recipes, key = { it.id }) { recipe ->
                customItem(recipe)
            }
        }
    }
}