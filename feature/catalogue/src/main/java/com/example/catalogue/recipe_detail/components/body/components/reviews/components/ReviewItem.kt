package com.example.catalogue.recipe_detail.components.body.components.reviews.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.JustImage
import com.example.data.models.Review
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import com.example.components.theme.JustCookColorPalette
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.catalogue.R
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.*

@Composable
fun ReviewItem(
    review: Review,
    canDeleteReview: (Review) -> Boolean,
    onDeleteReview: (Long) -> Unit
) {
    val monthNames = MonthNames(
        january = stringResource(R.string.january),
        february = stringResource(R.string.february),
        march = stringResource(R.string.march),
        april = stringResource(R.string.april),
        may = stringResource(R.string.may),
        june = stringResource(R.string.june),
        july = stringResource(R.string.july),
        august = stringResource(R.string.august),
        september = stringResource(R.string.september),
        october = stringResource(R.string.october),
        november = stringResource(R.string.november),
        december = stringResource(R.string.december)
    )

    val dateFormat = LocalDateTime.Format {
        dayOfMonth()
        char(' ')
        monthName(monthNames)
        char(' ')
        year()
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        JustImage(
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = review.user.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = JustCookColorPalette.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .widthIn(max = 75.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row {
                    for(star in 1..review.rating.toInt()) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = JustCookColorPalette.colors.iconPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = review.date.format(dateFormat),
                    style = MaterialTheme.typography.labelSmall,
                    color = JustCookColorPalette.colors.textHelp,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (canDeleteReview(review)) {
                    IconButton(
                        onClick = { onDeleteReview(review.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_review),
                            tint = JustCookColorPalette.colors.textHelp,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = JustCookColorPalette.colors.textSecondary
            )
        }
    }
}