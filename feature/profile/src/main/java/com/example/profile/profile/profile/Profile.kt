package com.example.profile.profile.profile

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.components.JustImage
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.ErrorPage
import com.example.components.JustButton
import com.example.components.JustSurface
import com.example.components.LoadingOverlay
import com.example.components.LocalSharedTransitionScope
import com.example.components.OnTopButton
import com.example.components.RequireSignInPage
import com.example.components.theme.JustCookColorPalette
import com.example.data.repositories.ImageRepository
import com.example.data.repositories.LocalImageRepository
import com.example.data.services.auth.LocalTokenService
import com.example.data.services.auth.TokenService
import com.example.data.services.user.LocalUserService
import com.example.data.services.user.UserService
import com.example.profile.R
import com.example.profile.profile.components.ProfileActionItem
import com.example.profile.profile.components.UserName

@Composable
fun ProfilePage(
    onUserRecipesClick: (userId: Long) -> Unit,
    onCreateRecipeClick: () -> Unit,
    onModerationClick: () -> Unit,
) {
    val userService = LocalUserService.current!!
    val imageRepository = LocalImageRepository.current!!
    val tokenService = LocalTokenService.current!!
    val context = LocalContext.current

    var isSignedIn by remember { mutableStateOf(tokenService.isSighedIn()) }
    var isSeriousError by remember { mutableStateOf(false) }

    RequireSignInPage(
        isSignedIn = isSignedIn,
        onSignIn = { isSignedIn = true }
    ) {
        if (isSeriousError) {
            ErrorPage(stringResource(R.string.default_error_message))
        } else {
            Profile(
                userService = userService,
                imageRepository = imageRepository,
                tokenService = tokenService,
                onUserRecipesClick = onUserRecipesClick,
                onCreateRecipeClick = onCreateRecipeClick,
                onModerationClick = onModerationClick,
                onLogout = { isSignedIn = false },
                onError = { Toast.makeText(context, context.resources.getString(R.string.default_error_message), Toast.LENGTH_LONG).show() },
                onSeriousError = { isSeriousError = true }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Profile(
    userService: UserService,
    imageRepository: ImageRepository,
    tokenService: TokenService,
    onUserRecipesClick: (userId: Long) -> Unit,
    onCreateRecipeClick: () -> Unit,
    onModerationClick: () -> Unit,
    onLogout: () -> Unit,
    onError: (String?) -> Unit,
    onSeriousError: (String?) -> Unit,
    isInEditModeDefault: Boolean = false,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.provideFactory(
        userService = userService,
        imageRepository = imageRepository,
        tokenService = tokenService
    ))
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")

    LaunchedEffect(true) {
        profileViewModel.onLogout = onLogout
        profileViewModel.onError = onError
        profileViewModel.onSeriousError = onSeriousError
        profileViewModel.isInEditMode = isInEditModeDefault
        profileViewModel.loadUser()
    }

    with(sharedTransitionScope) {
        JustSurface(Modifier.fillMaxSize()) {
            LoadingOverlay(uiState.isLoading) {
                Box(Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        JustImage(
                            image = uiState.user.image,
                            contentDescription = null,
                            isVerified = uiState.user.isVerified,
                            isEditable = uiState.isInEditMode,
                            onImageChange = profileViewModel::onImageChange,
                            modifier = Modifier
                                .size(150.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Имя и почта
                        UserName(
                            user = uiState.user,
                            uiState.isInEditMode,
                            onNameChange = profileViewModel::onNameChange
                        )
                        Text(
                            text = uiState.user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = JustCookColorPalette.colors.textHelp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        JustButton(
                            enabled = uiState.isValid,
                            onClick = profileViewModel::onEditToggle
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.isInEditMode) stringResource(R.string.stop_editing) else stringResource(R.string.edit_profile))
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Действия
                        ProfileActionItem(
                            icon = rememberVectorPainter(Icons.AutoMirrored.Filled.List),
                            label = stringResource(R.string.my_recipes),
                            onClick = { onUserRecipesClick(uiState.user.id) }
                        )
                        ProfileActionItem(
                            icon = rememberVectorPainter(Icons.Default.Add),
                            label = stringResource(R.string.create_recipe),
                            onClick = onCreateRecipeClick
                        )
                        if (uiState.canModerate) {
                            ProfileActionItem(
                                icon = rememberVectorPainter(Icons.Default.Person),
                                label = stringResource(R.string.moderation_button),
                                onClick = onModerationClick
                            )
                        }
                        ProfileActionItem(
                            icon = painterResource(R.drawable.logout),
                            label = stringResource(R.string.logout),
                            onClick = profileViewModel::onLogoutClick
                        )
                    }

                    if (uiState.isInEditMode) {
                        OnTopButton(
                            onPress = profileViewModel::onCancelEdit,
                            painter = rememberVectorPainter(Icons.Default.Clear),
                            contentDescription = stringResource(R.string.cancel_edit),
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }
    }
}