package com.eneko.gastospersonales.ui.theme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 🎨 Paleta de colores oscuros
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // Púrpura suave
    secondary = Color(0xFF03DAC6), // Verde azulado
    tertiary = Color(0xFFCF6679), // Rojo coral
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// 🎨 Paleta de colores claros
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Azul eléctrico
    secondary = Color(0xFF03DAC6), // Verde azulado
    tertiary = Color(0xFFB00020), // Rojo fuerte
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

// 🎨 Aplicar tema
@Composable
fun GastosPersonalesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
        typography = CustomTypography,
        content = content
    )
}

// 🖋️ Tipografía personalizada
val CustomTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 22.sp,
        color = Color.Black
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        color = Color.DarkGray
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        color = Color.Gray
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        color = Color.LightGray
    )
)
