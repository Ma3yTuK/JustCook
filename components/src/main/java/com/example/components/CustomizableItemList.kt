package com.example.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe

@Composable
fun <T> CustomizableItemList(
    entities: List<T>,
    customItem: @Composable LazyItemScope.(T) -> Unit,
    key: (T) -> Long,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        if (entities.isEmpty()) {
            item(key = "nothing") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_recipes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = JustCookColorPalette.colors.textHelp
                    )
                }
            }
        } else {
            items(entities, key = key) { entity ->
                customItem(entity)
            }
        }
    }
}