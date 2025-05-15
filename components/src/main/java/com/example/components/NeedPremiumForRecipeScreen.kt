package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.components.theme.JustCookColorPalette
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextButton

@Composable
fun PremiumAccessRequiredScreen(
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Золотая иконка или эмблема
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = JustCookColorPalette.colors.iconGold, // fallback if needed
            modifier = Modifier
                .size(72.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = JustCookColorPalette.colors.gradientGold
                    ),
                    shape = CircleShape
                )
                .padding(16.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Это премиум-рецепт",
            style = MaterialTheme.typography.titleLarge,
            color = JustCookColorPalette.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Оформите подписку, чтобы получить доступ к эксклюзивным рецептам от лучших поваров.",
            style = MaterialTheme.typography.bodyLarge,
            color = JustCookColorPalette.colors.textHelp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        TextButton(onClick = onGoBack) {
            Text("Вернуться назад")
        }
    }
}