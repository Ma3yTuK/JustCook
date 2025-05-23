package com.example.catalogue.recipe_detail.components.body.components.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.data.models.Review
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.components.reviews.components.ReviewItem
import com.example.catalogue.recipe_detail.components.body.components.reviews.components.add_review_bottom_sheet.AddReviewBottomSheet
import com.example.components.CustomizableItemList
import com.example.components.theme.JustCookColorPalette
import kotlinx.coroutines.flow.Flow

@Composable
fun Reviews(
    onSubmitReview: (rating: Long, comment: String) -> Unit,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val showSheet = remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.reviews_title),
                style = MaterialTheme.typography.titleLarge,
                color = JustCookColorPalette.colors.textPrimary
            )

            if(isLoggedIn) {
                IconButton(
                    onClick = { showSheet.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_review),
                        tint = JustCookColorPalette.colors.textHelp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showSheet.value) {
        AddReviewBottomSheet(
            onDismiss = { showSheet.value = false },
            onSubmitReview = onSubmitReview
        )
    }
}