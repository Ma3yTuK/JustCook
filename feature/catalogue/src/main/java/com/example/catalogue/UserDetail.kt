package com.example.catalogue

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.components.JustImage
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe
import com.example.data.models.User
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import com.example.components.JustDivider
import com.example.components.JustSurface
import com.example.components.OnTopButton
import com.example.components.RecipeList
import com.example.components.entity_collection_view.components.UserSharedElementKey
import com.example.components.entity_collection_view.components.UserSharedElementType
import com.example.components.entity_collection_view.components.userDetailBoundsTransform

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserDetail(
    user: User,
    collectionId: Long,
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onFavoriteClick: (Recipe) -> Unit,
    upPress: () -> Unit,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")

    with(sharedTransitionScope) {
        JustSurface(Modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                JustImage(
                    contentDescription = null,
                    isVerified = user.isVerified,
                    modifier = Modifier
                        .size(150.dp)
                        .sharedBounds(
                            rememberSharedContentState(
                                key = UserSharedElementKey(
                                    userId = user.id,
                                    type = UserSharedElementType.Image,
                                    collectionId = collectionId
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            exit = fadeOut(),
                            enter = fadeIn(),
                            boundsTransform = userDetailBoundsTransform
                        ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = JustCookColorPalette.colors.textPrimary,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(
                            key = UserSharedElementKey(
                                userId = user.id,
                                type = UserSharedElementType.Title,
                                collectionId = collectionId
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = userDetailBoundsTransform
                    )
                        .widthIn(max = 300.dp)
                )
                Text(
                    text = if (user.isVerified) stringResource(R.string.status_verified) else stringResource(R.string.status_not_verified),
                    style = MaterialTheme.typography.bodyMedium,
                    color = JustCookColorPalette.colors.textHelp
                )

                Spacer(modifier = Modifier.height(24.dp))
                JustDivider()
                Spacer(modifier = Modifier.height(24.dp))

                RecipeList(recipes, onRecipeClick, onFavoriteClick, isLoggedIn)
            }
            OnTopButton(
                onPress = upPress,
                painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ArrowBack),
                contentDescription = stringResource(R.string.label_back)
            )
        }
    }
}