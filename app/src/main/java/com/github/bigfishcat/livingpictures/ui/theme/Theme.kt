package com.github.bigfishcat.livingpictures.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Active,
    secondary = Inactive,
    tertiary = Selected,
    background = Background,
    onBackground = Active,
    surface = Background,
    onSurface = Active
)

private val LightColorScheme = lightColorScheme(
    primary = Active,
    secondary = Inactive,
    tertiary = Selected,
    background = Background,
    onBackground = Active,
    surface = Background,
    onSurface = Active
)

@Composable
fun LivingPicturesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}