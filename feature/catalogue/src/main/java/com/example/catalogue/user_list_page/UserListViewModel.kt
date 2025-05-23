package com.example.catalogue.user_list_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.catalogue.collections.CollectionDescription
import com.example.data.models.query_params.RecipeFilters
import com.example.data.models.query_params.RecipeFiltersDefault
import com.example.data.models.query_params.UserFilters
import com.example.data.models.query_params.UserFiltersDefault
import com.example.data.models.short_models.RecipeShort
import com.example.data.models.short_models.UserShort
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.getFavoritePagingSource
import com.example.data.services.recipe.getPagingSource
import com.example.data.services.user.UserService
import com.example.data.services.user.getPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
data class FilteredUserListRoute(
    override val isVerified: Boolean?,
    override val title: String
) : UserFiltersDefault(), CollectionDescription

data class UserUiState(
    val dataFlow: Flow<PagingData<UserShort>> = emptyFlow()
)

class UserListViewModel(
    private val userService: UserService,
) : ViewModel() {
    private val _userListUiState = MutableStateFlow(UserUiState())
    val userListUiState = _userListUiState.asStateFlow()
    private var pagingSource  = userService.getPagingSource(UserFiltersDefault(), null)
    var filters: UserFilters = UserFiltersDefault()

    private val pager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            pagingSource = userService.getPagingSource(filters, null)
            pagingSource
        }
    )

    fun refresh() {
        pagingSource.invalidate()
        _userListUiState.update {
            it.copy(
                dataFlow = pager.flow.cachedIn(viewModelScope)
            )
        }
    }

    companion object {
        fun provideFactory(
            userService: UserService,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserListViewModel(
                    userService = userService
                ) as T
            }
        }
    }
}