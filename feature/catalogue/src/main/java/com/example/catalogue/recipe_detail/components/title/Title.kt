package com.example.catalogue.recipe_detail.components.title

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.HzPadding
import com.example.catalogue.recipe_detail.MaxTitleOffset
import com.example.catalogue.recipe_detail.MinTitleOffset
import com.example.catalogue.recipe_detail.RecipeSharedElementKey
import com.example.catalogue.recipe_detail.RecipeSharedElementType
import com.example.catalogue.recipe_detail.TitleHeight
import com.example.catalogue.recipe_detail.components.title.components.RecipeCalories
import com.example.catalogue.recipe_detail.components.title.components.RecipeName
import com.example.catalogue.recipe_detail.components.title.components.Stars
import com.example.catalogue.recipe_detail.recipeDetailBoundsTransform
import com.example.components.JustDivider
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Title(
    recipe: Recipe,
    collectionId: Long,
    isInEditMode: Boolean,
    onNameChange: (String) -> Unit,
    scrollProvider: () -> Int
) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(JustCookColorPalette.colors.uiBackground)
    ) {
        Spacer(Modifier.height(16.dp))
        Stars(recipe, collectionId)
        Spacer(Modifier.height(5.dp))
        RecipeName(recipe, collectionId, isInEditMode, onNameChange)
        Spacer(Modifier.height(5.dp))
        RecipeCalories(recipe, collectionId, isInEditMode)
        Spacer(Modifier.height(5.dp))
        JustDivider(modifier = Modifier)
    }
}