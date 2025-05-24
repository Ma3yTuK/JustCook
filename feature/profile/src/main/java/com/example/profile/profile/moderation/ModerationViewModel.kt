package com.example.profile.profile.moderation

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.Image
import com.example.data.models.User
import com.example.data.models.short_models.RecipeShort
import com.example.data.repositories.ImageRepository
import com.example.data.services.DEFAULT_MAX_SIZE
import com.example.data.services.DEFAULT_PAGING_SIZE
import com.example.data.services.MyPagingSource
import com.example.data.services.auth.TokenService
import com.example.data.services.recipe.RecipeService
import com.example.data.services.recipe.UploadRecipeRequest
import com.example.data.services.recipe.getFavoritePagingSource
import com.example.data.services.recipe.getUnVerifiedPagingSource
import com.example.data.services.user.UploadUserRequest
import com.example.data.services.user.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class FavoriteUiState(
    var dataFlow: Flow<PagingData<RecipeShort>> = emptyFlow()
)

class ModerationViewModel(
    private val recipeService: RecipeService,
) : ViewModel() {
    private val _moderationUiState = MutableStateFlow(FavoriteUiState())
    val moderationUiState = _moderationUiState.asStateFlow()
    private var pagingSource  = recipeService.getUnVerifiedPagingSource()

    private val pager = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGING_SIZE.toInt(),
            maxSize = DEFAULT_MAX_SIZE.toInt(),
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            pagingSource = recipeService.getUnVerifiedPagingSource()
            pagingSource
        }
    )

    fun refresh() {
        pagingSource.invalidate()
        _moderationUiState.update {
            it.copy(
                dataFlow = pager.flow.cachedIn(viewModelScope)
            )
        }
    }

    companion object {
        fun provideFactory(
            recipeService: RecipeService
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ModerationViewModel(
                    recipeService = recipeService
                ) as T
            }
        }
    }
}