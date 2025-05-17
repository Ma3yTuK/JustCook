package com.example.profile.profile

import android.net.Uri
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.components.JustImage
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.components.JustButton
import com.example.components.JustSurface
import com.example.components.LocalSharedTransitionScope
import com.example.components.OnTopButton
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Authorities
import com.example.profile.R
import com.example.profile.profile.components.ProfileActionItem
import com.example.profile.profile.components.UserName

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Profile(
    user: User,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onLogoutClick: () -> Unit,
    onMyRecipesClick: () -> Unit,
    onCreateRecipeClick: () -> Unit,
    onImageChange: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onModerationClick: () -> Unit,
    canModerate: Boolean,
    isValid: Boolean,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    var isInEditMode by remember { mutableStateOf(false) }

    with(sharedTransitionScope) {
        JustSurface(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    JustImage(
                        contentDescription = null,
                        isVerified = user.isVerified,
                        isEditable = isInEditMode,
                        onImageChange = onImageChange,
                        modifier = Modifier
                            .size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Имя и почта
                    UserName(
                        user = user,
                        isInEditMode,
                        onNameChange
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = JustCookColorPalette.colors.textHelp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    JustButton(
                        enabled = isValid,
                        onClick = { onSaveEdit(); isInEditMode = !isInEditMode }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isInEditMode) stringResource(R.string.stop_editing) else stringResource(R.string.edit_profile))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Действия
                    ProfileActionItem(
                        icon = rememberVectorPainter(Icons.AutoMirrored.Filled.List),
                        label = stringResource(R.string.my_recipes),
                        onClick = onMyRecipesClick
                    )
                    ProfileActionItem(
                        icon = rememberVectorPainter(Icons.Default.Add),
                        label = stringResource(R.string.create_recipe),
                        onClick = onCreateRecipeClick
                    )
                    if (canModerate) {
                        ProfileActionItem(
                            icon = rememberVectorPainter(Icons.Default.Person),
                            label = stringResource(R.string.moderation_button),
                            onClick = onModerationClick
                        )
                    }
                    ProfileActionItem(
                        icon = painterResource(R.drawable.logout),
                        label = stringResource(R.string.logout),
                        onClick = onLogoutClick
                    )
                }

                if (isInEditMode) {
                    OnTopButton(
                        onPress = { onCancelEdit(); isInEditMode = !isInEditMode },
                        painter = rememberVectorPainter(Icons.Default.Clear),
                        contentDescription = stringResource(R.string.cancel_edit),
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}