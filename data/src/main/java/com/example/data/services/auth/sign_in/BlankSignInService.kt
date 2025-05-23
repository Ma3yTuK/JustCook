package com.example.data.services.auth.sign_in

import com.example.data.services.auth.TokenService
import com.example.data.services.auth.AuthService
import com.example.data.services.auth.LoginRequest

class BlankSignInService(
    private val authService: AuthService,
    private val tokenService: TokenService
): SignInService {
    override suspend fun onSignIn(callback: () -> Unit, onError: (Exception) -> Unit) {
        try {
            //tokenService.setToken(authService.login(LoginRequest(userId = "12341234")).token)
            tokenService.setToken(authService.loginEmail("maria.smirnova@example.com").token)
            callback()
        } catch(e: Exception) {
            onError(e)
        }
    }
}