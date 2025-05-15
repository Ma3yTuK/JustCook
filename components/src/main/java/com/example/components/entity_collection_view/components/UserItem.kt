package com.example.components.entity_collection_view.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.components.springs.nonSpatialExpressiveSpring
import com.example.components.LocalNavAnimatedVisibilityScope
import com.example.components.LocalSharedTransitionScope
import com.example.data.models.Recipe
import com.example.components.JustImage
import com.example.components.JustSurface
import com.example.components.springs.spatialExpressiveSpring
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.User

data class UserSharedElementKey(
    val userId: Long,
    val type: UserSharedElementType,
    val collectionId: Long = 0
)

enum class UserSharedElementType {
    Image,
    Title
}

@OptIn(ExperimentalSharedTransitionApi::class)
val userDetailBoundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun UserItem(
    collectionId: Long,
    user: User,
    onUserClick: (User, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    JustSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
            ?: throw IllegalStateException("No sharedTransitionScope found")
        val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
            ?: throw IllegalStateException("No animatedVisibilityScope found")

        with(sharedTransitionScope) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = {
                        onUserClick(user, collectionId)
                    })
                    .padding(8.dp)
            ) {
                JustImage(
                    elevation = 1.dp,
                    contentDescription = null,
                    isVerified = user.isVerified,
                    modifier = Modifier
                        .size(120.dp)
                        .sharedBounds(
                            rememberSharedContentState(
                                key = UserSharedElementKey(
                                    userId = user.id,
                                    type = UserSharedElementType.Image,
                                    collectionId = collectionId
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = userDetailBoundsTransform
                        )
                )
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = JustCookColorPalette.colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .widthIn(max = 100.dp)
                        .sharedBounds(
                            rememberSharedContentState(
                                key = UserSharedElementKey(
                                    userId = user.id,
                                    type = UserSharedElementType.Title,
                                    collectionId = collectionId
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            enter = fadeIn(nonSpatialExpressiveSpring()),
                            exit = fadeOut(nonSpatialExpressiveSpring()),
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                            boundsTransform = userDetailBoundsTransform
                        )
                )
            }
        }
    }
}