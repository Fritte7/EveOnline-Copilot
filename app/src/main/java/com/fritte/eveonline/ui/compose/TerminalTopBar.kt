package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.fritte.eveonline.theme.EveColors

@Composable
fun TerminalTopBar(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        subtitle?.let {
            Spacer(Modifier.height(4.dp))
            Text(
                text = it,
                color = EveColors.Outline,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
