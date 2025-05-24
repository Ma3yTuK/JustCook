package com.example.search.search_page.components.search_bar.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Ingredient
import com.example.data.models.User
import com.example.data.models.categories.CategoryCollection
import com.example.data.models.categories.LifeStyleCategory
import com.example.data.models.categories.RealCategory
import com.example.data.models.short_models.UserShort
import com.example.search.R
import com.example.search.search_page.RecipeSortingType
import com.example.search.search_page.SortingType
import com.example.search.search_page.UserSortingType
import com.example.search.search_page.components.search_bar.components.filter.components.FilterChipSection
import com.example.search.search_page.components.search_bar.components.filter.components.SearchSelectionSection
import com.example.search.search_page.components.search_bar.components.filter.components.SelectionSection
import com.example.search.search_state.SearchState
import com.example.search.search_state.SearchType
import kotlinx.coroutines.flow.Flow

@Composable
fun Filter(
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onResetFilters: () -> Unit,
    searchState: SearchState,
    onStateChange: (SearchState) -> Unit,
    searchTypes: List<SearchType>,
    sortingTypes: List<SortingType>,
    categories: List<RealCategory>,
    lifestyles: List<LifeStyleCategory>,
    userSearchQuery: String,
    onUserQueryChange: (String) -> Unit,
    foundUsers: Flow<PagingData<UserShort>>,
    ingredientSearchQuery: String,
    onIngredientQueryChange: (String) -> Unit,
    foundIngredients: Flow<PagingData<Ingredient>>,
) {
    val resources = LocalContext.current.resources

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // capture click
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDismiss()
                }
        )
        Column(
            Modifier
                .padding(16.dp)
                .align(Alignment.Center)
                .clip(MaterialTheme.shapes.medium)
                .wrapContentSize()
                .heightIn(max = 450.dp)
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
                .background(JustCookColorPalette.colors.uiFloated)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()
            ) {
                IconButton(onClick = onApply) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
                Text(
                    text = stringResource(id = R.string.label_filters),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = onResetFilters
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }

            SelectionSection(
                title = stringResource(R.string.search_type),
                display = { resources.getString(it.displayedNameId) },
                onClickOption = { onStateChange(searchState.copy(searchType = it)) },
                selected = { searchState.searchType == it },
                selections = searchTypes
            )
            SelectionSection(
                title = stringResource(R.string.sorting),
                display = { it.display },
                onClickOption = { onStateChange(searchState.copy(
                    sortingOption = it.field,
                    sortingAscending = it.sortingAscending
                )) },
                selected = { searchState.sortingOption == it.field },
                selections = sortingTypes
            )
            if (searchState.searchType == SearchType.Recipe) {
                FilterChipSection(
                    filterCollection = CategoryCollection(stringResource(R.string.real_categories_header), categories),
                    searchState = searchState,
                    onStateChange = onStateChange
                )
                FilterChipSection(
                    filterCollection = CategoryCollection(stringResource(R.string.lifestyles_header), lifestyles),
                    searchState = searchState,
                    onStateChange = onStateChange
                )
                SearchSelectionSection(
                    title = stringResource(R.string.by_user),
                    default = searchState.users,
                    getName = { user -> user.name },
                    setSelected = { user, value -> when {
                        (!value) -> onStateChange(searchState.copy(users = searchState.users - setOf(user)))
                        else -> onStateChange(searchState.copy(users = searchState.users + setOf(user)))
                    }},
                    query = userSearchQuery,
                    onQueryChange = onUserQueryChange,
                    availableChips = foundUsers
                )
                SearchSelectionSection(
                    title = stringResource(R.string.by_ingredients),
                    default = searchState.ingredients,
                    getName = { ingredient -> ingredient.name },
                    setSelected = { ingredient, value -> when {
                        (!value) -> onStateChange(searchState.copy(ingredients = searchState.ingredients - setOf(ingredient)))
                        else -> onStateChange(searchState.copy(ingredients = searchState.ingredients + setOf(ingredient)))
                    }},
                    query = ingredientSearchQuery,
                    onQueryChange = onIngredientQueryChange,
                    availableChips = foundIngredients
                )
            }
        }

    }
}