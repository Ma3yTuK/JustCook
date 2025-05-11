package com.example.catalogue.recipe_detail.components.body.components.ingredients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.components.ingredients.components.IngredientInfo
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.IngredientIngredientConversion
import com.example.data.models.RecipeIngredient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ingredients(
    ingredients: List<RecipeIngredient>,
    getConversionsForIngredient: (Ingredient) -> List<IngredientIngredientConversion>,
    setIngredientConversion: (RecipeIngredient, IngredientIngredientConversion?) -> Unit,
    setIngredientAmount: (RecipeIngredient, Float) -> Unit,
    onDeleteIngredient: (RecipeIngredient) -> Unit,
    addIngredient: (Ingredient, Float, IngredientIngredientConversion?) -> Unit,
    allIngredients: List<Ingredient>,
    isInEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    var showAddSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier) {
        // Заголовок
        Text(
            text = stringResource(R.string.ingredients),
            style = MaterialTheme.typography.titleLarge,
            color = JustCookColorPalette.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )

        Spacer(Modifier.height(4.dp))

        // Существующие ингредиенты
        ingredients.forEach { ingredient ->
            IngredientInfo(
                ingredient = ingredient,
                getConversionsForIngredient = getConversionsForIngredient,
                setIngredientConversion = setIngredientConversion,
                setIngredientAmount = setIngredientAmount,
                onDeleteIngredient = onDeleteIngredient,
                isInEditMode = isInEditMode
            )
        }

        // Кнопка «Добавить ингредиент»
        if (isInEditMode) {
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = { showAddSheet = true },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_ingredient)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(R.string.add_ingredient))
            }
        }
    }

    // BottomSheet для добавления нового ингредиента
    if (showAddSheet) {
        var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
        var expandedIngredient by remember { mutableStateOf(false) }
        var amount by remember { mutableStateOf("") }
        var selectedConversion by remember { mutableStateOf<IngredientIngredientConversion?>(null) }
        var expandedConversion by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_ingredient),
                    style = MaterialTheme.typography.titleMedium
                )

                // Выбор ингредиента
                ExposedDropdownMenuBox(
                    expanded = expandedIngredient,
                    onExpandedChange = { expandedIngredient = it }
                ) {
                    TextField(
                        value = selectedIngredient?.name ?: "",
                        onValueChange = {},
                        label = { Text(stringResource(R.string.ingredient)) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedIngredient) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedIngredient,
                        onDismissRequest = { expandedIngredient = false }
                    ) {
                        allIngredients.forEach { ing ->
                            DropdownMenuItem(
                                text = { Text(ing.name) },
                                onClick = {
                                    selectedIngredient = ing
                                    selectedConversion = null
                                    expandedIngredient = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedConversion,
                    onExpandedChange = { expandedConversion = it }
                ) {
                    TextField(
                        enabled = selectedIngredient != null,
                        value = selectedConversion?.measurementTo?.name
                            ?: selectedIngredient?.unit?.name ?: "",
                        onValueChange = {},
                        label = { Text(stringResource(R.string.unit)) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedConversion) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedConversion,
                        onDismissRequest = { expandedConversion = false }
                    ) {
                        val convOptions = listOf<IngredientIngredientConversion?>(null) +
                                getConversionsForIngredient(selectedIngredient!!)

                        convOptions.forEach { conv ->
                            val label = conv?.measurementTo?.name
                                ?: selectedIngredient!!.unit.name
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedConversion = conv
                                    expandedConversion = false
                                }
                            )
                        }
                    }
                }

                // Ввод количества
                OutlinedTextField(
                    enabled = selectedIngredient != null,
                    value = amount,
                    onValueChange = { input ->
                        input.toFloatOrNull()?.let { amount = input }
                    },
                    label = { Text(stringResource(R.string.amount)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Кнопка «Сохранить»
            Button(
                onClick = {
                    val amt = amount.toFloatOrNull() ?: 0f
                    selectedIngredient?.let { ing ->
                        addIngredient(ing, amt, selectedConversion)
                    }
                    showAddSheet = false
                },
                enabled = selectedIngredient != null && amount.toFloatOrNull() != null,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}