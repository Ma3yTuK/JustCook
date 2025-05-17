package com.example.search.search_page.components.search_bar.components.filter.components.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette

@Composable
fun FilterTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = JustCookColorPalette.colors.brand,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}