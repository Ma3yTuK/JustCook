package com.example.catalogue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RecipeList
import com.example.components.UserList
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.Recipe
import com.example.data.models.User

@Composable
fun UserListPage(
    title: String,
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    JustSurface(Modifier.fillMaxSize()) {
        Column {
            PageTitle(title)
            UserList(users, onUserClick)
        }
    }
}