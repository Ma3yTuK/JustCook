package com.example.search.search_page.components.search_bar.components.filter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.IngredientIngredientConversion
import com.example.search.R
import com.example.search.search_page.components.search_bar.components.filter.components.components.Chip
import com.example.search.search_page.components.search_bar.components.filter.components.components.FilterTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchSelectionSection(
    title: String,
    getName: (T) -> String,
    setSelected: (T, Boolean) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    availableChips: List<T>
) {
    var showSheet by remember { mutableStateOf(false) }
    var selectedChips: List<T> by remember { mutableStateOf(listOf()) }

    FilterTitle(text = title)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)
            .padding(horizontal = 4.dp)
    ) {
        selectedChips.forEach { chip ->
            Chip(
                name = getName(chip),
                selected = true,
                setSelected = { setSelected(chip, false); selectedChips = selectedChips.filter { it != chip } },
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp).align(Alignment.CenterVertically)
            )
        }
        IconButton(
            onClick = { showSheet = true },
            modifier = Modifier
                .clip(CircleShape)
                .size(20.dp)
                .background(JustCookColorPalette.colors.iconPrimary)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                tint = JustCookColorPalette.colors.uiBackground,
                contentDescription = stringResource(R.string.add_chip),
            )
        }
    }

    if (showSheet) {
        var selected by remember { mutableStateOf<T?>(null) }
        var expanded by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_chip_label),
                    style = MaterialTheme.typography.titleMedium
                )

                // Выбор ингредиента
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        value = query,
                        onValueChange = onQueryChange,
                        label = { Text(stringResource(R.string.add_filter)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        availableChips.forEach { chip ->
                            val chipName = getName(chip)

                            DropdownMenuItem(
                                text = { Text(chipName) },
                                onClick = {
                                    selected = chip
                                    onQueryChange(chipName)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Кнопка «Сохранить»
            Button(
                onClick = {
                    selectedChips = selectedChips + listOf(selected!!)
                    setSelected(selected!!, true)
                    showSheet = false
                },
                enabled = selected != null,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(com.example.catalogue.R.string.save))
            }
        }
    }
}