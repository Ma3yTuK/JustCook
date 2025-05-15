package com.example.catalogue

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.JustDivider
import com.example.components.JustSurface
import com.example.components.entity_collection_view.EntityCollectionView
import com.example.data.models.Recipe
import com.example.data.models.User
import com.example.data.models.collections.EntityCollection

@Composable
fun Feed(
    collections: List<EntityCollection>,
    onRecipeClick: (Recipe, Long) -> Unit,
    onUserClick: (User, Long) -> Unit,
    onCollectionClick: (EntityCollection) -> Unit,
    modifier: Modifier = Modifier
) {
    JustSurface(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(collections) { index, collection ->
                if (index > 0) {
                    JustDivider(thickness = 2.dp)
                }

                EntityCollectionView(
                    entityCollection = collection,
                    onCollectionClick = onCollectionClick,
                    onRecipeClick = onRecipeClick,
                    onUserClick = onUserClick
                )
            }
        }
    }
}