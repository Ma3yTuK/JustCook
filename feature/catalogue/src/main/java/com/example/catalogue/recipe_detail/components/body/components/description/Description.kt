package com.example.catalogue.recipe_detail.components.body.components.description

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.HzPadding
import com.example.catalogue.recipe_detail.components.body.components.description.components.TextButton
import com.example.components.LocalSharedTransitionScope
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun Description(
    recipe: Recipe,
    onChangeDescription: (String) -> Unit,
    isInEditMode: Boolean
) {
    val sharedTransitionScope =
        LocalSharedTransitionScope.current ?: throw IllegalStateException("No scope found")

    var isExpanded by remember { mutableStateOf(false) }
    var isTextOverflowing by remember { mutableStateOf(false) }

    var showEditSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var editedDescription by remember { mutableStateOf(recipe.description) }

    with(sharedTransitionScope) {
        Column {
            Row(
                modifier = HzPadding
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.detail_header),
                    style = MaterialTheme.typography.labelSmall,
                    color = JustCookColorPalette.colors.textHelp,
                    modifier = Modifier.weight(1f)
                )

                if (isInEditMode) {
                    IconButton(onClick = {
                        editedDescription = recipe.description
                        showEditSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit),
                            tint = JustCookColorPalette.colors.textHelp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyLarge,
                color = JustCookColorPalette.colors.textHelp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis,
                modifier = HzPadding.skipToLookaheadSize(),
                onTextLayout = {
                    if (!isExpanded) {
                        isTextOverflowing = it.hasVisualOverflow
                    }
                }
            )

            if (isTextOverflowing || isExpanded) {
                val textButton = if (!isExpanded) {
                    stringResource(id = R.string.see_more)
                } else {
                    stringResource(id = R.string.see_less)
                }

                TextButton(
                    buttonText = textButton,
                    onButtonClick = { isExpanded = !isExpanded }
                )
            }
        }
    }

    if (showEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_description),
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )

                Button(
                    onClick = {
                        onChangeDescription(editedDescription)
                        showEditSheet = false
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}