package com.example.horta.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

//Cores do figma
val VerdePrincipal = Color(0xFF12A423)      // #12A423
val VerdeEscuro = Color(0xFF286619)         // #286619
val VerdeClaro = Color(0xFFC4D3C1) // #C4D3C1
val AmareloDestaque = Color(0xFFDECA45)     // #DECA45
val CinzaClaro = Color(0xFFB7B7B7)          // #B7B7B7
val CinzaEscuro = Color(0xFF2E2E2E)         // #2E2E2E
val CardBackgroundColor = Color(0xFFF5F5F5)  // Cinza bem claro (quase branco)
val CardBackgroundColor2 = Color(0xFFFAFAFA)

private val DarkColorScheme = darkColorScheme(
    primary = VerdePrincipal,
    secondary = VerdeEscuro,
    tertiary = AmareloDestaque,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    secondary = VerdeEscuro,
    tertiary = AmareloDestaque,
    background = Color.White,
    surface = Color.White
)

// Tipografia para fazer responsividade
val DinamicTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = AmareloDestaque
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        color = AmareloDestaque
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = Color.White
    ),
    bodyLarge = TextStyle(
        fontSize = 22.sp,
        color = Color.Black
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        color = Color.Black
    ),
    bodySmall = TextStyle( //me parece inutil
        fontSize = 17.sp,
        color = Color(0xFFB7B7B7)
    )
)

@Composable
fun HortaTheme(
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DinamicTypography,
        content = content
    )
}