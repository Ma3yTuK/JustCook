package com.example.catalogue.recipe_detail.components.body.components.reviews.components.add_review_bottom_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.components.reviews.components.add_review_bottom_sheet.components.RatingStars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewBottomSheet(
    onSubmitReview: (rating: Float, comment: String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableFloatStateOf(0f) }
    var comment by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.add_review),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            RatingStars(rating = rating, onRatingSelected = { rating = it })

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text(stringResource(R.string.review_comment_hint)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    onSubmitReview(rating, comment)
                    onDismiss()
                },
                enabled = rating > 0 && comment.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.submit_review))
            }
        }
    }
}
