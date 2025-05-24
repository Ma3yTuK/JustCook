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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import kotlin.math.max
import com.example.components.JustImage
import com.example.components.VerticalGrid
import com.example.data.models.categories.Category
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.search.R

@Composable
fun SearchCategories(
    categories: List<RealCategory>,
    lifestyles: List<LifeStyleCategory>,
    onCategoryClick: (Category) -> Unit
) {
    LazyColumn {
        item {
            SearchCategoryCollection(CategoryCollection(stringResource(R.string.real_categories_header), categories), 0, onCategoryClick)
        }
        item {
            SearchCategoryCollection(CategoryCollection(stringResource(R.string.lifestyles_header), lifestyles), 1, onCategoryClick)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun SearchCategoryCollection(
    collection: CategoryCollection,
    index: Int,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = collection.name,
            style = MaterialTheme.typography.titleLarge,
            color = JustCookColorPalette.colors.textPrimary,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .wrapContentHeight()
        )
        VerticalGrid(Modifier.padding(horizontal = 16.dp)) {
            val gradient = when (index % 2) {
                0 -> JustCookColorPalette.colors.gradient2_2
                else -> JustCookColorPalette.colors.gradient2_3
            }
            collection.categories.forEach { category ->
                SearchCategory(
                    category = category,
                    gradient = gradient,
                    onCategoryClick =onCategoryClick,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

private val MinImageSize = 134.dp
private val CategoryShape = RoundedCornerShape(10.dp)
private const val CategoryTextProportion = 0.55f

@Composable
private fun SearchCategory(
    category: Category,
    gradient: List<Color>,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier
            .aspectRatio(1.45f)
            .shadow(elevation = 3.dp, shape = CategoryShape)
            .clip(CategoryShape)
            .background(Brush.horizontalGradient(gradient))
            .clickable { onCategoryClick(category) },
        content = {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                color = JustCookColorPalette.colors.textSecondary,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 8.dp)
            )
            JustImage(
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    ) { measurables, constraints ->
        // Text given a set proportion of width (which is determined by the aspect ratio)
        val textWidth = (constraints.maxWidth * CategoryTextProportion).toInt()
        val textPlaceable = measurables[0].measure(Constraints.fixedWidth(textWidth))

        // Image is sized to the larger of height of item, or a minimum value
        // i.e. may appear larger than item (but clipped to the item bounds)
        val imageSize = max(MinImageSize.roundToPx(), constraints.maxHeight)
        val imagePlaceable = measurables[1].measure(Constraints.fixed(imageSize, imageSize))
        layout(
            width = constraints.maxWidth,
            height = constraints.minHeight
        ) {
            textPlaceable.placeRelative(
                x = 0,
                y = (constraints.maxHeight - textPlaceable.height) / 2 // centered
            )
            imagePlaceable.placeRelative(
                // image is placed to end of text i.e. will overflow to the end (but be clipped)
                x = textWidth,
                y = (constraints.maxHeight - imagePlaceable.height) / 2 // centered
            )
        }
    }
}
