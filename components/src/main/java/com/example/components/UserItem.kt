package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.User

@Composable
fun UserItem(
    user: User,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick(user) }
            .background(JustCookColorPalette.colors.uiBackground)
            .padding(horizontal = 24.dp)

    ) {
        val (divider, image, name, statusSpacer, status) = createRefs()
        createVerticalChain(name, status, chainStyle = ChainStyle.Packed)
        JustImage(
            contentDescription = null,
            isVerified = user.isVerified,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = user.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            color = JustCookColorPalette.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                width = Dimension.preferredWrapContent
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Spacer(
            Modifier
                .height(8.dp)
                .constrainAs(statusSpacer) {
                    linkTo(top = name.bottom, bottom = status.top)
                }
        )
        Text(
            text = if (user.isVerified) stringResource(R.string.status_verified) else stringResource(R.string.status_not_verified),
            style = MaterialTheme.typography.bodyLarge,
            color = JustCookColorPalette.colors.textHelp,
            modifier = Modifier.constrainAs(status) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        JustDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}