package com.example.catalogue.user_list_page

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import com.example.catalogue.R
import com.example.catalogue.collections.CollectionDescription
import com.example.catalogue.recipe_list_page.FilteredRecipeListRoute
import com.example.components.JustSurface
import com.example.components.PageTitle
import com.example.components.RequireSignInPage
import com.example.components.UserList
import com.example.data.models.query_params.UserFiltersDefault
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.recipe.LocalRecipeService
import com.example.data.services.user.LocalUserService
import com.example.data.services.user.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Composable
fun UserListPage(
    route: FilteredUserListRoute,
    onUserClick: (UserShort) -> Unit
) {
    val userService = LocalUserService.current!!

    UserListPage(
        route = route,
        userService = userService,
        onUserClick = onUserClick,
    )
}

@Composable
fun UserListPage(
    route: FilteredUserListRoute,
    userService: UserService,
    onUserClick: (UserShort) -> Unit,
    viewModel: UserListViewModel = viewModel(factory = UserListViewModel.provideFactory(userService))
) {
    val uiState by viewModel.userListUiState.collectAsState()

    LaunchedEffect(route) {
        viewModel.filters = route
        viewModel.refresh()
    }

    JustSurface(Modifier.fillMaxSize()) {
        Column {
            PageTitle(route.title)
            UserList(uiState.dataFlow, onUserClick)
        }
    }
}