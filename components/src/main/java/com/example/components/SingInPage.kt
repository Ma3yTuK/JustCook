package com.example.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.components.theme.JustCookColorPalette
import com.example.data.services.auth.sign_in.LocalSignInService
import com.example.data.services.auth.LocalTokenService
import kotlinx.coroutines.launch

@Composable
fun RequireSignInPage(
    onSignIn: () -> Unit = {},
    isSignedIn: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val signInService = LocalSignInService.current!!
    val coroutineScope = rememberCoroutineScope()
    if (isSignedIn) {
        content()
    } else {
        JustSurface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFAFAFA), Color(0xFFE0E0E0))
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Card(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(containerColor = JustCookColorPalette.colors.uiFloated)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.app_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .padding(16.dp),
                            tint = Color.Unspecified
                        )

                        Text(
                            text = "Welcome to JustCook",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = JustCookColorPalette.colors.textSecondary
                        )

                        GoogleSignInButton(onClick = {
                            coroutineScope.launch {
                                signInService.onSignIn(
                                    callback = onSignIn,
                                    onError = { Toast.makeText(context, it.message, Toast.LENGTH_LONG).show() }
                                )
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = JustCookColorPalette.colors.iconPrimary),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "Google Icon",
            tint = JustCookColorPalette.colors.iconInteractive,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Login with Google",
            color = JustCookColorPalette.colors.textInteractive,
            fontWeight = FontWeight.Medium
        )
    }
}