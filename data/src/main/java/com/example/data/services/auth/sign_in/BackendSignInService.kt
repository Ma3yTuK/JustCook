package com.example.data.services.auth.sign_in

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.data.R
import com.example.data.services.auth.AuthService
import com.example.data.services.auth.LoginRequest
import com.example.data.services.auth.TokenService
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class BackendSignInService(
    private val context: Context,
    private val authService: AuthService,
    private val tokenService: TokenService
) : SignInService {
    private val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
        .Builder(context.resources.getString(R.string.web_client_id))
        .build()
    private val googleRequest: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()
    private val credentialManager: CredentialManager = CredentialManager.create(context)

    override suspend fun onSignIn(callback: () -> Unit, onError: (Exception) -> Unit) {
        try {
            val result = credentialManager.getCredential(
                request = googleRequest,
                context = context,
            )
            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)
            tokenService.setToken(authService.login(LoginRequest(userId = googleIdTokenCredential.idToken)).token)
            callback()
        } catch (e: Exception) {
            onError(e)
        }
    }
}