package com.eneko.gastospersonales.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = CardBackgroundColor,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextColor,
    onSurface = TextColor
)

val CustomTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 24.sp,
        color = TextColor
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        color = DarkGray
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        color = DarkGray
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        color = LightGray
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        color = Color.White
    )
)

@Composable
fun GastosPersonalesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}
