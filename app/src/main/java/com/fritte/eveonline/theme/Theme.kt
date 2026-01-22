package com.fritte.eveonline.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EveTerminalScheme = darkColorScheme(
    primary = EveColors.Primary,
    onPrimary = EveColors.OnPrimary,

    background = EveColors.Bg,
    onBackground = EveColors.OnBg,

    surface = EveColors.Surface,
    onSurface = EveColors.OnSurface,

    surfaceVariant = EveColors.SurfaceVariant,
    onSurfaceVariant = EveColors.OnSurface,

    outline = EveColors.Outline,

    error = EveColors.Error,
    onError = Color.Black,
)

@Composable
fun EveOnlineCopilotTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EveTerminalScheme,
        typography = Typography,
        content = content
    )
}
