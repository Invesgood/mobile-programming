package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NoblesseColorScheme = lightColorScheme(
    primary = Gold,
    onPrimary = DarkBackground,
    secondary = AccentRed,
    onSecondary = CreamBackground,
    background = CreamBackground,
    onBackground = TextDark,
    surface = CreamBackground,
    onSurface = TextDark
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NoblesseColorScheme,
        typography = Typography,
        content = content
    )
}