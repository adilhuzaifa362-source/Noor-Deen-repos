package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NoorDeenDarkColorScheme = darkColorScheme(
    primary = GoldMetallic,
    onPrimary = Emerald900,
    primaryContainer = Emerald700,
    onPrimaryContainer = GoldLight,
    secondary = Emerald500,
    onSecondary = Color.White,
    secondaryContainer = Emerald800,
    onSecondaryContainer = Emerald100,
    tertiary = GoldLight,
    onTertiary = Emerald900,
    tertiaryContainer = GoldContainerDark,
    onTertiaryContainer = GoldLight,
    background = CharcoalDark,
    onBackground = Color(0xFFE4E9E9),
    surface = SurfaceDark,
    onSurface = Color(0xFFE4E9E9),
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFB0BDC0),
    outline = OutlineDark
)

private val NoorDeenLightColorScheme = lightColorScheme(
    primary = Emerald700,
    onPrimary = Color.White,
    primaryContainer = Emerald50,
    onPrimaryContainer = Emerald800,
    secondary = GoldDark,
    onSecondary = Color.White,
    secondaryContainer = GoldContainerLight,
    onSecondaryContainer = GoldDark,
    tertiary = Emerald600,
    onTertiary = Color.White,
    tertiaryContainer = Emerald100,
    onTertiaryContainer = Emerald900,
    background = IvoryLight,
    onBackground = Color(0xFF161E1F),
    surface = SurfaceLight,
    onSurface = Color(0xFF161E1F),
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF4C5D60),
    outline = OutlineLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NoorDeenDarkColorScheme else NoorDeenLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
