package com.example.catalogue.recipe_detail.components.body.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.components.JustImage
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.User
import com.example.data.models.short_models.UserShort

@Composable
fun Author(
    user: UserShort,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JustImage(
            image = user.image,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(40.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "Автор рецепта",
                style = MaterialTheme.typography.labelSmall,
                color = JustCookColorPalette.colors.textHelp
            )
            Text(
                text = user.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = JustCookColorPalette.colors.textPrimary
            )
        }
    }
}