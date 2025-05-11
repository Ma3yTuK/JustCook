package com.example.profile.profile

import androidx.compose.runtime.Composable
import com.example.data.models.User
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.components.JustImage
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.components.JustButton
import com.example.components.theme.JustCookColorPalette
import com.example.profile.R
import com.example.profile.profile.components.ProfileActionItem

@Composable
fun Profile(
    user: User,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onMyRecipesClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        JustImage(
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Имя и почта
        Text(
            text = user.name,
            style = MaterialTheme.typography.titleMedium,
            color = JustCookColorPalette.colors.textPrimary
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = JustCookColorPalette.colors.textHelp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Редактировать"
        JustButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.edit_profile))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Действия
        ProfileActionItem(
            icon = rememberVectorPainter(Icons.AutoMirrored.Filled.List),
            label = stringResource(R.string.my_recipes),
            onClick = onMyRecipesClick
        )
        ProfileActionItem(
            icon = rememberVectorPainter(Icons.Default.Favorite),
            label = stringResource(R.string.favorites),
            onClick = onFavoritesClick
        )
        ProfileActionItem(
            icon = painterResource(R.drawable.logout),
            label = stringResource(R.string.logout),
            onClick = onLogoutClick
        )
    }
}