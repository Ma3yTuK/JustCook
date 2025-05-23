package com.example.catalogue.recipe_detail.components.body.components.ingredients.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.res.stringResource
import com.example.catalogue.R
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import com.example.data.models.IngredientUnitConversion
import com.example.data.models.RecipeConversion

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInfo(
    ingredient: RecipeConversion,
    updateConversionsForIngredient: () -> Unit,
    conversionsForIngredient: List<IngredientUnitConversion>?,
    updateIngredient: (IngredientUnitConversion, Long) -> Unit,
    onDeleteIngredient: () -> Unit,
    isInEditMode: Boolean
) {
    var showEditSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp)
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ingredient.ingredientUnitConversion.ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            color = JustCookColorPalette.colors.textHelp,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
        )

        Text(
            text = String.format(
                "%.1f %s",
                ingredient.amount * (ingredient.ingredientUnitConversion.coefficient),
                ingredient.ingredientUnitConversion.unit.name
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = JustCookColorPalette.colors.textHelp
        )

        if (isInEditMode) {
            IconButton(
                modifier = Modifier
                    .width(30.dp)
                    .padding(start = 10.dp),
                onClick = { showEditSheet = true; updateConversionsForIngredient() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit),
                    tint = JustCookColorPalette.colors.textHelp,
                )
            }

            IconButton(
                modifier = Modifier.width(30.dp),
                onClick = { onDeleteIngredient() }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = JustCookColorPalette.colors.error,
                )
            }
        }
    }

    if (showEditSheet) {
        var editedAmount by remember { mutableLongStateOf(ingredient.amount) }
        var editedConversion by remember { mutableStateOf(ingredient.ingredientUnitConversion) }
        var expanded by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = ingredient.ingredientUnitConversion.ingredient.name,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = editedAmount.toString(),
                    onValueChange = { input ->
                        input.toLongOrNull()?.let { editedAmount = it }
                    },
                    enabled = conversionsForIngredient != null,
                    label = { Text(stringResource(R.string.amount)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    val label = editedConversion.unit.name

                    TextField(
                        value = label,
                        onValueChange = {},
                        readOnly = true,
                        enabled = conversionsForIngredient != null,
                        label = { Text(stringResource(R.string.unit)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        conversionsForIngredient?.forEach { conv ->
                            val title = conv.unit.name
                            DropdownMenuItem(
                                text = { Text(title) },
                                onClick = {
                                    editedConversion = conv
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        updateIngredient(editedConversion, editedAmount)
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