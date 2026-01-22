package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.fritte.eveonline.theme.EveColors

@Composable
fun TerminalScaffold(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TerminalHeader(title = title)
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
fun TerminalHeader(title: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            color = EveColors.PrimaryVariant,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Initializing interface …",
            color = EveColors.Outline,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TerminalPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = EveColors.Surface,
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Composable
fun TerminalRow(
    label: String,
    value: String,
    valueColor: Color = EveColors.PrimaryVariant,
    dotsMin: Int = 6
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            color = EveColors.PrimaryVariant,
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.width(8.dp))

        // dot fill to the right - not perfect typography, but gives the vibe
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = ".".repeat(dotsMin).repeat(6), // cheap filler
                color = EveColors.Warn.copy(alpha = 0.55f),
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = value,
            color = valueColor,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TerminalLine(text: String, color: Color = EveColors.PrimaryVariant) {
    Text(
        text = text,
        color = color,
        fontFamily = FontFamily.Monospace,
        style = MaterialTheme.typography.bodyMedium
    )
}

fun statusColor(status: String): Color = when (status.uppercase()) {
    "OK", "READY", "ONLINE", "PRESENT" -> EveColors.PrimaryVariant
    "OFFLINE", "PAUSED" -> EveColors.Error
    "WARN", "CRITICAL" -> EveColors.Warn
    "ERROR", "FAIL" -> EveColors.Error
    else -> EveColors.PrimaryVariant
}
