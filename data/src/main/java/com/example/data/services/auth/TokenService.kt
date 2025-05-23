package com.example.data.services.auth
import android.content.Context
import android.content.IntentSender.OnFinished
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import androidx.core.content.edit
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.data.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.coroutineScope

private const val tokenName: String = "token"
private const val preferencesName: String = "preferences"

class TokenService(
    private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    fun setToken(token: String) {
        sharedPreferences.edit { putString(tokenName, token) }
    }

    fun removeToken() {
        sharedPreferences.edit { remove(tokenName) }
    }

    fun getToken(): String {
        return sharedPreferences.getString(tokenName, "")!!
    }

    fun isSighedIn(): Boolean {
        return sharedPreferences.contains(tokenName)
    }
}

val LocalTokenService = compositionLocalOf<TokenService?> { null }