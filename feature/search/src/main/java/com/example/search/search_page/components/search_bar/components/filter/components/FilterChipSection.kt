package com.example.search.search_page.components.search_bar.components.filter.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.components.JustSurface
import com.example.components.theme.JustCookColorPalette
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.components.modifiers.offsetGradientBackground
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.search.search_page.components.search_bar.components.filter.components.components.Chip
import com.example.search.search_page.components.search_bar.components.filter.components.components.FilterTitle
import com.example.search.search_state.SearchState

@Composable
fun FilterChipSection(
    filterCollection: CategoryCollection,
    searchState: SearchState,
    onStateChange: (SearchState) -> Unit
) {
    FilterChipSection(
        title = filterCollection.name,
        getName = { chip -> chip.name },
        selected = { chip -> when {
            (chip is LifeStyleCategory) -> searchState.lifeStyleIds.contains(chip.id)
            (chip is RealCategory) -> searchState.categoryIds.contains(chip.id)
            else -> false
        }},
        setSelected = { chip, value -> when {
            (chip is LifeStyleCategory && value) -> onStateChange(searchState.copy(lifeStyleIds = searchState.lifeStyleIds + listOf(chip.id)))
            (chip is LifeStyleCategory) -> onStateChange(searchState.copy(lifeStyleIds = searchState.lifeStyleIds.filter { it != chip.id }))
            (chip is RealCategory && value) -> onStateChange(searchState.copy(categoryIds = searchState.categoryIds + listOf(chip.id)))
            (chip is RealCategory) -> onStateChange(searchState.copy(categoryIds = searchState.categoryIds.filter { it != chip.id }))
        }},
        chips = filterCollection.categories
    )
}

@Composable
fun <T> FilterChipSection(
    title: String,
    getName: (T) -> String,
    selected: (T) -> Boolean,
    setSelected: (T, Boolean) -> Unit,
    chips: List<T>
) {
    FilterTitle(text = title)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp)
            .padding(horizontal = 4.dp)
    ) {
        chips.forEach { chip ->
            Chip(
                name = getName(chip),
                selected = selected(chip),
                setSelected = { if (selected(chip) != it) setSelected(chip, it) },
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp).align(Alignment.CenterVertically)
            )
        }
    }
}