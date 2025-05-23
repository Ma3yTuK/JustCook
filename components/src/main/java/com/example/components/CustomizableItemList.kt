package com.example.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.EntityWithId
import com.example.data.models.Recipe
import kotlinx.coroutines.flow.Flow

@Composable
fun <T: EntityWithId> CustomizableItemList(
    entities: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    updatedItems: Map<Long, T> = mapOf(),
    removedItems: Set<Long> = setOf(),
    customItem: @Composable LazyItemScope.(T) -> Unit,
) {
    val lazyPagingItems = entities.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
    ) {
        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = stringResource(R.string.loading),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
        if (lazyPagingItems.itemCount == 0 && lazyPagingItems.loadState.isIdle || lazyPagingItems.loadState.hasError) {
            item(key = "nothing") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if(lazyPagingItems.loadState.hasError) stringResource(R.string.error_list) else stringResource(R.string.no_recipes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = JustCookColorPalette.colors.textHelp
                    )
                }
            }
        } else {
            items(
                lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id }
            ) { index ->
                val entity = lazyPagingItems[index]
                if (entity != null && entity.id !in removedItems) {
                    customItem(updatedItems[entity.id] ?: entity)
                }
            }
        }
    }
}