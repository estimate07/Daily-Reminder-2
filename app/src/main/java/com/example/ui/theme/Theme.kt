package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EditorialColorScheme = darkColorScheme(
    primary = SignalRed,
    secondary = Sage,
    tertiary = Clay,
    background = Desk,
    surface = Paper,
    onBackground = Paper,
    onSurface = Ink,
    outline = Ink,
    surfaceVariant = DeskSurface,
    onSurfaceVariant = MutedText
)

@Composable
fun RovioDailyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EditorialColorScheme,
        typography = AppTypography,
        content = content
    )
}
