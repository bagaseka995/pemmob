package com.kunkun.secondshop.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Tema utama aplikasi KunkunSecondShop

// Color Scheme untuk mode Terang (Light)
private val LightColorScheme = lightColorScheme(
    primary            = Teal40,
    onPrimary          = Grey99,
    primaryContainer   = Teal90,
    onPrimaryContainer = Teal20,

    secondary          = Navy40,
    onSecondary        = Grey99,
    secondaryContainer = Navy90,
    onSecondaryContainer = Navy30,

    tertiary           = Amber40,
    onTertiary         = Grey99,
    tertiaryContainer  = Amber90,
    onTertiaryContainer = Amber30,

    error              = Red40,
    onError            = Grey99,
    errorContainer     = Red90,
    onErrorContainer   = Red10,

    background         = Grey99,
    onBackground       = Grey10,
    surface            = Grey99,
    onSurface          = Grey10,
    surfaceVariant     = TealGrey90,
    onSurfaceVariant   = TealGrey30,
    outline            = TealGrey50,
    outlineVariant     = TealGrey80,
)

// Color Scheme untuk mode Gelap (Dark)
private val DarkColorScheme = darkColorScheme(
    primary            = Teal80,
    onPrimary          = Teal20,
    primaryContainer   = Teal30,
    onPrimaryContainer = Teal90,

    secondary          = Navy80,
    onSecondary        = Navy20,
    secondaryContainer = Navy30,
    onSecondaryContainer = Navy90,

    tertiary           = Amber80,
    onTertiary         = Amber30,
    tertiaryContainer  = Amber30,
    onTertiaryContainer = Amber90,

    error              = Red80,
    onError            = Red20,
    errorContainer     = Red20,
    onErrorContainer   = Red90,

    background         = Grey10,
    onBackground       = Grey90,
    surface            = Grey10,
    onSurface          = Grey90,
    surfaceVariant     = TealGrey20,
    onSurfaceVariant   = TealGrey80,
    outline            = TealGrey60,
    outlineVariant     = TealGrey20,
)

// Tema Compose dengan dukungan light dan dark mode
@Composable
fun KunkunSecondShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
        typography = KunkunTypography,
        content = content
    )
}
