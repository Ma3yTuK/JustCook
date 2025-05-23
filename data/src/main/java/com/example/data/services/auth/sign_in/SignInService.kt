package com.example.data.services.auth.sign_in

import androidx.compose.runtime.compositionLocalOf

interface SignInService {
    suspend fun onSignIn(callback: () -> Unit, onError: (Exception) -> Unit = {})
}

val LocalSignInService = compositionLocalOf<SignInService?> { null }