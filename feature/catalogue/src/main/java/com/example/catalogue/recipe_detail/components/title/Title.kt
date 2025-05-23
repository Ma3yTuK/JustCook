package com.example.catalogue.recipe_detail.components.title

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.catalogue.recipe_detail.MaxTitleOffset
import com.example.catalogue.recipe_detail.MinTitleOffset
import com.example.catalogue.recipe_detail.TitleHeight
import com.example.catalogue.recipe_detail.components.title.components.RecipeCalories
import com.example.catalogue.recipe_detail.components.title.components.RecipeName
import com.example.catalogue.recipe_detail.components.body.components.RecipeWeight
import com.example.catalogue.recipe_detail.components.title.components.Stars
import com.example.components.JustDivider
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@Composable
fun Title(
    recipe: Recipe,
    collectionIndex: Int?,
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
        Spacer(Modifier.height(20.dp))
        Stars(recipe, collectionIndex)
        Spacer(Modifier.height(7.dp))
        RecipeName(recipe, collectionIndex, isInEditMode, onNameChange)
        Spacer(Modifier.height(7.dp))
        RecipeCalories(recipe, collectionIndex)
        Spacer(Modifier.height(7.dp))
        JustDivider(modifier = Modifier)
    }
}