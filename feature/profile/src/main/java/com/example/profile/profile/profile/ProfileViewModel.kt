package com.example.profile.profile.profile

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.models.Authorities
import com.example.data.models.Authority
import com.example.data.models.Image
import com.example.data.models.User
import com.example.data.repositories.ImageRepository
import com.example.data.services.auth.TokenService
import com.example.data.services.recipe.UploadRecipeRequest
import com.example.data.services.user.UploadUserRequest
import com.example.data.services.user.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class ProfileUiState(
    val isInEditMode: Boolean = false,
    val user: User = User(
        id = -1,
        name = "",
        email = "",
        isVerified = false,
        image = null,
        authorities = listOf()
    ),
    val isLoading: Boolean = false
) {
    val isValid: Boolean
        get() = user.name.isNotBlank()
                && user.email.isNotBlank()
                && user.id >= 0

    val canModerate: Boolean
        get() = user.authorities.any { it.id == Authorities.Moderate.id }
}

class ProfileViewModel(
    private val userService: UserService,
    private val imageRepository: ImageRepository,
    private val tokenService: TokenService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())

    var onLogout: () -> Unit = {}
    var onError: (String?) -> Unit = {}
    var onSeriousError: (String?) -> Unit = {}
    var isInEditMode: Boolean
        get() = _uiState.value.isInEditMode
        set(value) = _uiState.update { it.copy(isInEditMode = value) }

    private var fallbackName: String? = null
    private var fallbackImage: Image? = null

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUser() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(user = userService.getCurrentUser(), isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                if (e is HttpException && e.code() == 401) {
                    onLogoutClick()
                } else {
                    onSeriousError(e.message)
                }
            }
        }
    }

    fun onEditToggle() {
        if (uiState.value.isInEditMode)
            onSaveEdit()
        else
            onEdit()
    }

    private fun onEdit() {
        _uiState.update { state ->
            fallbackName = state.user.name
            fallbackImage = state.user.image
            state.copy(
                isInEditMode = true
            )
        }
    }

    fun onCancelEdit() {
        _uiState.update {
            it.copy(
                isInEditMode = false,
                user = it.user.copy(
                    name = fallbackName!!,
                    image = fallbackImage
                ),
            )
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    name = newName
                ),
            )
        }
    }

    fun onImageChange(uri: Uri) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        user = it.user.copy(image = imageRepository.uploadImage(uri)),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError(e.message)
            }
        }
    }

    private fun onSaveEdit() {
        val state = _uiState.value
        if (!state.isValid) return
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        user = userService.updateCurrent(UploadUserRequest(it.user)),
                        isLoading = false,
                        isInEditMode = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError(e.message)
            }
        }
    }

    fun onLogoutClick() {
        tokenService.removeToken()
        onLogout()
    }

    companion object {
        fun provideFactory(
            userService: UserService,
            tokenService: TokenService,
            imageRepository: ImageRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(
                    imageRepository = imageRepository,
                    tokenService = tokenService,
                    userService = userService
                ) as T
            }
        }
    }
}