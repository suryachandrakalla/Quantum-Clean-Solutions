package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = QuantumBlue,
    onPrimary = Color.White,
    primaryContainer = QuantumNavy,
    onPrimaryContainer = Color.White,
    secondary = QuantumMint,
    onSecondary = Color.White,
    secondaryContainer = QuantumEmerald,
    background = SurfaceDark,
    surface = SurfaceDarkCard,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = QuantumNavy,
    onPrimary = Color.White,
    primaryContainer = QuantumLightBlue,
    onPrimaryContainer = QuantumNavy,
    secondary = QuantumEmerald,
    onSecondary = Color.White,
    secondaryContainer = QuantumLightGreen,
    background = SurfaceLight,
    surface = SurfaceCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun QuantumCleanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
