package com.example.soffisushi.ui.theme

import android.view.Window
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = Red,
    onPrimary = Color.White
)

val ColorScheme.success: Color
    get() = Green
val ColorScheme.onSuccess: Color
    get() = White

@Composable
fun SoffiSushiTheme(
    window: Window,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

    window.statusBarColor = colorScheme.background.toArgb()
    window.navigationBarColor = colorScheme.surfaceContainer.toArgb()

    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).run {
        isAppearanceLightStatusBars = false
        isAppearanceLightNavigationBars = false
    }
}
