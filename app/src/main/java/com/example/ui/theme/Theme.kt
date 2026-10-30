package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TimbaktuPurple,
    onPrimary = Color.White,
    primaryContainer = TimbaktuPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = TimbaktuAmber,
    onSecondary = Color.Black,
    secondaryContainer = TimbaktuSurfaceVariant,
    onSecondaryContainer = Color.White,
    tertiary = TimbaktuPink,
    background = TimbaktuDarkBg,
    onBackground = TextPrimary,
    surface = TimbaktuSurface,
    onSurface = TextPrimary,
    surfaceVariant = TimbaktuSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = TimbaktuFlame
)

private val LightColorScheme = DarkColorScheme // Timbaktu is a premier dark-themed video shorts platform

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
