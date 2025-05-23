package com.example.catalogue.recipe_detail.components.body.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.HzPadding
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementKey
import com.example.components.entity_collection_view.components.recipe_item.RecipeSharedElementType
import com.example.components.entity_collection_view.components.recipe_item.recipeDetailBoundsTransform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeWeight(
    recipe: Recipe,
    isInEditMode: Boolean,
    onWeightChange: (Long) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var editedWeight by remember { mutableStateOf(recipe.weight.toString()) }

    Row(
        modifier = HzPadding
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(R.string.weigh_label)} ${recipe.weight} ${stringResource(R.string.grams)}",
            fontSize = 15.sp,
            style = MaterialTheme.typography.labelSmall,
            color = JustCookColorPalette.colors.textHelp,
            modifier = Modifier.weight(1f)
        )

        if (isInEditMode) {
            IconButton(onClick = {
                editedWeight = recipe.weight.toString()
                showSheet = true
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit),
                    tint = JustCookColorPalette.colors.textHelp
                )
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_recipe_weight),
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = editedWeight,
                    onValueChange = { newWeight ->
                        newWeight.toLongOrNull()?.let { editedWeight = newWeight }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        onWeightChange(editedWeight.toLong())
                        showSheet = false
                    },
                    enabled = editedWeight.toLongOrNull()?.let { true } ?: false,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}