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
    primary = EmeraldPrimary,
    onPrimary = ImmersiveBg,
    primaryContainer = EmeraldContainerSolid,
    onPrimaryContainer = EmeraldLight,
    secondary = AccentTeal,
    onSecondary = ImmersiveBg,
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFF99F6E4),
    tertiary = AccentOrange,
    onTertiary = ImmersiveBg,
    tertiaryContainer = Color(0xFF7C2D12),
    onTertiaryContainer = Color(0xFFFFEDD5),
    background = ImmersiveBg,
    onBackground = TextWhite,
    surface = ImmersiveSurface,
    onSurface = TextWhite,
    surfaceVariant = ImmersiveSurfaceVariant,
    onSurfaceVariant = TextSlate,
    outline = ImmersiveBorder
)

private val LightColorScheme = darkColorScheme( // Immersive UI enforces the signature immersive dark aesthetic
    primary = EmeraldPrimary,
    onPrimary = ImmersiveBg,
    primaryContainer = EmeraldContainerSolid,
    onPrimaryContainer = EmeraldLight,
    secondary = AccentTeal,
    onSecondary = ImmersiveBg,
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFF99F6E4),
    tertiary = AccentOrange,
    onTertiary = ImmersiveBg,
    tertiaryContainer = Color(0xFF7C2D12),
    onTertiaryContainer = Color(0xFFFFEDD5),
    background = ImmersiveBg,
    onBackground = TextWhite,
    surface = ImmersiveSurface,
    onSurface = TextWhite,
    surfaceVariant = ImmersiveSurfaceVariant,
    onSurfaceVariant = TextSlate,
    outline = ImmersiveBorder
)


@Composable
fun RaqeebTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our signature Raqeeb cybersecurity & marketing palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias for any existing tests
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    RaqeebTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
