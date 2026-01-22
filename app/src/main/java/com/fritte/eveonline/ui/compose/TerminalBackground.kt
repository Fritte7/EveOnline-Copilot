package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.theme.EveColors

@Composable
fun TerminalBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.background)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(EveColors.SurfaceVariant, EveColors.BgCenter),
                        radius = 900f
                    )
                )
                .blur(24.dp)
        )
        content()
    }
}
