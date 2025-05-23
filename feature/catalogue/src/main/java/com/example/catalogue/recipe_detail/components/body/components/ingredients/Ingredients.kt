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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.components.ingredients.components.IngredientInfo
import com.example.components.CustomizableItemList
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.IngredientUnitConversion
import com.example.data.models.RecipeConversion
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ingredients(
    ingredients: List<RecipeConversion>,
    ingredientQuery: String,
    onIngredientQueryChange: (String) -> Unit,
    updateConversionsForIngredient: (Long) -> Unit,
    updateIngredient: (Int, IngredientUnitConversion?, Long?) -> Unit,
    onDeleteIngredient: (Int) -> Unit,
    addIngredient: (IngredientUnitConversion, Long) -> Unit,
    allIngredients: List<Ingredient>,
    conversionsForIngredient: List<IngredientUnitConversion>?,
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
        ingredients.forEachIndexed { index, ingredient ->
            IngredientInfo(
                ingredient = ingredient,
                updateConversionsForIngredient = { updateConversionsForIngredient(ingredient.ingredientUnitConversion.ingredient.id) },
                conversionsForIngredient = conversionsForIngredient,
                updateIngredient = { conversion, amount -> updateIngredient(index, conversion, amount)},
                onDeleteIngredient = { onDeleteIngredient(index) },
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
        var selectedConversion by remember { mutableStateOf<IngredientUnitConversion?>(null) }
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
                        value = ingredientQuery,
                        onValueChange = onIngredientQueryChange,
                        label = { Text(stringResource(R.string.ingredient)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedIngredient) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedIngredient,
                        onDismissRequest = { expandedIngredient = false }
                    ) {
                        allIngredients.forEach {
                            DropdownMenuItem(
                                text = { Text(it.name) },
                                onClick = {
                                    updateConversionsForIngredient(it.id)
                                    selectedIngredient = it
                                    selectedConversion = null
                                    expandedIngredient = false
                                    onIngredientQueryChange(it.name)
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
                        enabled = conversionsForIngredient != null,
                        value = selectedConversion?.unit?.name ?: "",
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
                        conversionsForIngredient?.forEach {
                            val label = it.unit.name
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedConversion = it
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
                        input.toLongOrNull()?.let { amount = input }
                    },
                    label = { Text(stringResource(R.string.amount)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Кнопка «Сохранить»
            Button(
                onClick = {
                    addIngredient(selectedConversion!!, amount.toLongOrNull() ?: 0L)
                    showAddSheet = false
                },
                enabled = amount.toLongOrNull() != null && selectedConversion != null,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}