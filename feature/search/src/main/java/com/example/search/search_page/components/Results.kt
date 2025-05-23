/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.search.search_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.PagingData
import com.example.components.CustomizableItemList
import com.example.components.JustButton
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe
import com.example.components.JustDivider
import com.example.components.JustImage
import com.example.components.RecipeItem
import com.example.components.UserItem
import com.example.data.models.EntityWithId
import com.example.data.models.User
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.search.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchResults(
    searchResults: Flow<PagingData<EntityWithId>>,
    onRecipeClick: (Long) -> Unit,
    onToggleFavoriteClick: (RecipeShort) -> Unit,
    onUserClick: (Long) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.search_results),
            style = MaterialTheme.typography.titleLarge,
            color = JustCookColorPalette.colors.textPrimary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )
        CustomizableItemList(
            entities = searchResults,
            customItem = { entityWithId ->
                if (entityWithId is RecipeShort) {
                    RecipeItem(
                        recipe = entityWithId,
                        onRecipeClick = { onRecipeClick(it.id) },
                        onIconClick = onToggleFavoriteClick,
                        iconPainter = if (entityWithId.isFavorite) rememberVectorPainter(Icons.Default.Favorite) else rememberVectorPainter(Icons.Default.FavoriteBorder)
                    )
                }
                if (entityWithId is UserShort) {
                    UserItem(
                        user = entityWithId,
                        onUserClick = { onUserClick(it.id) }
                    )
                }
            }
        )
    }
}

@Composable
fun NoResults(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(24.dp)
    ) {
        Image(
            painterResource(R.drawable.empty_state_search),
            contentDescription = null
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.search_no_matches),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.search_no_matches_retry),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}