package com.example.search.search_page.components.search_bar.components.filter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.EntityWithId
import com.example.data.models.Ingredient
import com.example.search.R
import com.example.search.search_page.components.search_bar.components.filter.components.components.Chip
import com.example.search.search_page.components.search_bar.components.filter.components.components.FilterTitle
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: EntityWithId> SearchSelectionSection(
    title: String,
    default: Set<T>,
    getName: (T) -> String,
    setSelected: (T, Boolean) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    availableChips: Flow<PagingData<T>>
) {
    val lazyPagingItems = availableChips.collectAsLazyPagingItems()
    var showSheet by remember { mutableStateOf(false) }
    var selectedChips: Set<T> by remember { mutableStateOf(default) }

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
                setSelected = { setSelected(chip, false); selectedChips = selectedChips - setOf(chip) },
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
                    val sizeOfOneItem by remember {
                        mutableStateOf(50.dp)
                    }
                    val screenSize = LocalWindowInfo.current.containerSize
                    val screenHeight50 by remember {
                        mutableStateOf(screenSize.height.dp / 2)
                    }
                    val itemsSize = sizeOfOneItem * lazyPagingItems.itemCount
                    val height by remember(lazyPagingItems.itemCount) {
                        mutableStateOf(minOf(itemsSize, screenHeight50))
                    }
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .width(screenSize.width.dp)
                                .height(height)
                        ) {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = lazyPagingItems.itemKey { it.id }
                            ) { index ->
                                val chip = lazyPagingItems[index]
                                if (chip != null) {
                                    val chipName = getName(chip)
                                    DropdownMenuItem(
                                        text = { Text(chipName) },
                                        onClick = {
                                            selected = chip
                                            onQueryChange(chipName)
                                            expanded = false
                                        },
                                        modifier = Modifier.height(50.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Кнопка «Сохранить»
            Button(
                onClick = {
                    selectedChips = selectedChips + setOf(selected!!)
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