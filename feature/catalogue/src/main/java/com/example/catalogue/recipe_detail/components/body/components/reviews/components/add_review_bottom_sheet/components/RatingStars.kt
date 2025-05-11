package com.example.catalogue.recipe_detail.components.body.components.reviews.components.add_review_bottom_sheet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.example.catalogue.R
import com.example.components.theme.JustCookColorPalette

@Composable
fun RatingStars(rating: Float, onRatingSelected: (Float) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(5) { index ->
            val filled = index < rating
            IconButton(onClick = { onRatingSelected((index + 1).toFloat()) }) {
                Icon(
                    painter = if (filled) rememberVectorPainter(Icons.Default.Star) else painterResource(R.drawable.star_outline),
                    contentDescription = null,
                    tint = JustCookColorPalette.colors.iconPrimary
                )
            }
        }
    }
}