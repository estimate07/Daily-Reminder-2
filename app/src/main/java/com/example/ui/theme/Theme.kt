package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppColors = staticCompositionLocalOf { ThemePresets[0] }

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

@Composable
fun RovioDailyTheme(
    themeIndex: Int = 0,
    content: @Composable () -> Unit
) {
    val currentColors = ThemePresets.getOrElse(themeIndex) { ThemePresets[0] }

    CompositionLocalProvider(LocalAppColors provides currentColors) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                primary = currentColors.signalRed,
                secondary = currentColors.sage,
                tertiary = currentColors.clay,
                background = currentColors.desk,
                surface = currentColors.paper,
                onBackground = currentColors.paper,
                onSurface = currentColors.ink,
                outline = currentColors.ink,
                surfaceVariant = currentColors.deskSurface,
                onSurfaceVariant = currentColors.mutedText
            ),
            typography = AppTypography,
            content = content
        )
    }
}
