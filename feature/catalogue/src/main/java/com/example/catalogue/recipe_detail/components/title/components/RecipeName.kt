package com.example.catalogue.recipe_detail.components.title.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.HzPadding
import com.example.catalogue.recipe_detail.RecipeSharedElementKey
import com.example.catalogue.recipe_detail.RecipeSharedElementType
import com.example.catalogue.recipe_detail.recipeDetailBoundsTransform
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun RecipeName(
    recipe: Recipe,
    collectionId: Long,
    isInEditMode: Boolean,
    onNameChange: (String) -> Unit
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalArgumentException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")

    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(recipe.name) }

    with(sharedTransitionScope) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = HzPadding
                .height(30.dp)
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineSmall,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .sharedBounds(
                        rememberSharedContentState(
                            key = RecipeSharedElementKey(
                                recipeId = recipe.id,
                                type = RecipeSharedElementType.Title,
                                collectionId = collectionId
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = recipeDetailBoundsTransform
                    )
            )

            if (isInEditMode) {
                IconButton(onClick = {
                    editedName = recipe.name
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
                    text = stringResource(R.string.edit_recipe_name),
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        onNameChange(editedName)
                        showSheet = false
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}