package com.example.components.entity_collection_view.components.recipe_item.components.card_contentent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.entity_collection_view.components.recipe_item.components.card_contentent.components.CardCalories
import com.example.components.entity_collection_view.components.recipe_item.components.card_contentent.components.CardRating
import com.example.components.entity_collection_view.components.recipe_item.components.card_contentent.components.CardTitle
import com.example.data.models.Recipe
import com.example.data.models.short_models.RecipeShort

@Composable
fun CardContent(
    recipe: RecipeShort,
    collectionIndex: Int
) {
    CardRating(recipe, collectionIndex)
    Spacer(modifier = Modifier.height(4.dp))
    CardTitle(recipe, collectionIndex)
    Spacer(modifier = Modifier.height(4.dp))
    CardCalories(recipe, collectionIndex)
}