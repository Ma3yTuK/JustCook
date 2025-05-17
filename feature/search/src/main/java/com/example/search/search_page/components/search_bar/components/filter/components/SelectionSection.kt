package com.example.search.search_page.components.search_bar.components.filter.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import com.example.search.R
import com.example.search.search_page.components.search_bar.components.filter.components.components.FilterTitle

@Composable
fun <T> SelectionSection(
    title: String,
    display: (T) -> String,
    onClickOption: (T) -> Unit,
    selected: (T) -> Boolean,
    selections: List<T>
) {
    FilterTitle(text = title)
    Column(Modifier.padding(bottom = 24.dp)) {
        selections.forEach { selection ->
            Option(
                text = display(selection),
                onClickOption = { if (!selected(selection)) onClickOption(selection) },
                selected = selected(selection)
            )
        }
    }
}

@Composable
private fun Option(
    text: String,
    onClickOption: () -> Unit,
    selected: Boolean,
    icon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .padding(top = 14.dp)
            .selectable(selected) { onClickOption() }
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = JustCookColorPalette.colors.brand
            )
        }
    }
}