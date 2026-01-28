package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.fritte.eveonline.theme.EveColors
import com.fritte.eveonline.ui.model.TerminalRowStatus.CRITICAL
import com.fritte.eveonline.ui.model.TerminalRowStatus.ERROR
import com.fritte.eveonline.ui.model.TerminalRowStatus.FAIL
import com.fritte.eveonline.ui.model.TerminalRowStatus.OFFLINE
import com.fritte.eveonline.ui.model.TerminalRowStatus.OK
import com.fritte.eveonline.ui.model.TerminalRowStatus.ONLINE
import com.fritte.eveonline.ui.model.TerminalRowStatus.PAUSED
import com.fritte.eveonline.ui.model.TerminalRowStatus.READY
import com.fritte.eveonline.ui.model.TerminalRowStatus.PRESENT
import com.fritte.eveonline.ui.model.TerminalRowStatus.WARN

@Composable
fun TerminalScaffold(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        content()
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
                text = ".".repeat(dotsMin).repeat(6),
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
        fontWeight = FontWeight.SemiBold
    )
}

fun statusColor(status: String): Color = when (status.uppercase()) {
    OK, READY, ONLINE, PRESENT -> EveColors.PrimaryVariant
    OFFLINE, PAUSED -> EveColors.Error
    WARN, CRITICAL -> EveColors.Warn
    ERROR, FAIL -> EveColors.Error
    else -> EveColors.PrimaryVariant
}

@Composable
fun TerminalKeypadBackground(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        color = EveColors.PanelBg,      // define or replace
        contentColor = EveColors.OnBg,  // define or replace
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .background(EveColors.PanelBg)
                .padding(contentPadding)
        ) {
            content()
        }
    }
}

@Composable
fun TerminalCommandLine(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type a command…"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(EveColors.Transparent)
            .border(1.dp, EveColors.OnBg.copy(alpha = 0.25f), shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ">",
            modifier = Modifier.padding(end = 8.dp),
            color = EveColors.OnBg,
            fontFamily = FontFamily.Monospace
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholder,
                    color = EveColors.OnBg.copy(alpha = 0.25f),
                    fontFamily = FontFamily.Monospace
                )
            },
            textStyle = TextStyle(
                color = EveColors.OnBg,
                fontFamily = FontFamily.Monospace
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = EveColors.Transparent,
                unfocusedContainerColor = EveColors.Transparent,
                disabledContainerColor = EveColors.Transparent,
                focusedIndicatorColor = EveColors.Transparent,
                unfocusedIndicatorColor = EveColors.Transparent,
                cursorColor = EveColors.OnBg
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSubmit() })
        )
    }
}

@Composable
fun TerminalKeypad(
    onNav: () -> Unit,
    onDb: () -> Unit,
    onSta: () -> Unit,
    onCfg: () -> Unit,
    onTim: () -> Unit,
    onTab: () -> Unit,
    onEnter: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        // Row 1
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Key("NAV", onNav)
            Key("DB", onDb)
            Key("STA", onSta)
            Key("TAB", onTab, highlighted = true)
        }

        // Row 2
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Key("CFG", onCfg)
            Key("TIM", onTim)
            EnterKey(onEnter)
        }
    }
}

@Composable
private fun Key(
    label: String,
    onClick: () -> Unit,
    highlighted: Boolean = false,
) {
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (highlighted) EveColors.KeyHighlight else EveColors.KeyBg
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = EveColors.OnBg,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RowScope.EnterKey(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(EveColors.KeyEnter)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ENTER",
            color = EveColors.OnBg,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}
