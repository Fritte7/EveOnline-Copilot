package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.theme.EveColors
import com.fritte.eveonline.ui.model.BootItem
import com.fritte.eveonline.ui.model.TerminalRowStatus
import com.fritte.eveonline.ui.model.toChunk
import com.fritte.eveonline.ui.states.StartupState
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import kotlinx.coroutines.delay

@Composable
fun BootScreen(
    modifier: Modifier = Modifier,
    vm: StartupViewModel
) {
    val state by vm.state.collectAsState()

    val priorityLines = remember { mutableStateListOf<BootItem>() }
    val scriptLinesRandom = remember {
        listOf(
            BootItem.Row("Loading probes", TerminalRowStatus.OK),
            BootItem.Row("Starting scanner", TerminalRowStatus.OK),
            BootItem.Row("Cleaning sensors", TerminalRowStatus.OK),
            BootItem.Row("Turning lights ON", TerminalRowStatus.OK),
            BootItem.Row("Decrypting bookmarks", TerminalRowStatus.OK),
            BootItem.Row("Loading Anoikis topology", TerminalRowStatus.OK),
            BootItem.Row("Cloaking device standby", TerminalRowStatus.OK),
            BootItem.Row("Handshake: CONCORD relay", TerminalRowStatus.OK),
            BootItem.Row("Neural link stabilization", TerminalRowStatus.OK),
            BootItem.Row("Calibrating probe launcher", TerminalRowStatus.OK),
            BootItem.Row("Gravimetric sensors online", TerminalRowStatus.OK),
            BootItem.Row("Initializing coffee injector", TerminalRowStatus.OK),
            BootItem.Row("Covert ops protocols loaded", TerminalRowStatus.OK),
            BootItem.Row("Injecting: \"Stay Neutral.\"", TerminalRowStatus.OK),
            BootItem.Row("Warp core integrity", TerminalRowStatus.STABLE),
            BootItem.Row("Caffeine reserves", TerminalRowStatus.CRITICAL),
            BootItem.Row("Towel inventory",TerminalRowStatus.PRESENT),
        )
    }

    val typer = rememberTerminalTyper()
    val displayText = typedTextWithCursor(typer.typedText, typer.isDone)

    val randomCount = 7
    val linePauseMs = 300L
    val initPauseMs = 1000L

    // State -> priority lines (use SHORT labels, not pre-dotted strings)
    LaunchedEffect(state) {
        when (state) {
            StartupState.ImportingStaticData ->
                priorityLines.add(BootItem.Row("Importing static data", TerminalRowStatus.OK))
            StartupState.ImportingVisitedSystemData ->
                priorityLines.add(BootItem.Row("Checking visited system", TerminalRowStatus.OK))
            StartupState.CheckingAuth ->
                priorityLines.add(BootItem.Row("Checking authentication", TerminalRowStatus.OK))
            is StartupState.Error ->
                priorityLines.add(BootItem.Row(TerminalRowStatus.ERROR, (state as StartupState.Error).message))
            else -> Unit
        }
    }

    // Terminal styling
    val terminalStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        color = EveColors.Primary
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val textMeasurer = rememberTextMeasurer()
        val density = LocalDensity.current
        val charWidthPx = remember {
            textMeasurer.measure("W", style = terminalStyle).size.width.coerceAtLeast(1)
        }

        val boxMaxWidth = this.maxWidth
        val columns = remember(boxMaxWidth, charWidthPx) {
            with(density) {
                boxMaxWidth.toPx().toInt() / charWidthPx
            }.coerceAtLeast(30)
        }

        suspend fun enqueueItems(items: List<BootItem>, isLastBlock: Boolean) {
            items.forEachIndexed { index, item ->
                val isLastLine = isLastBlock && index == items.lastIndex
                val suffix = if (!isLastLine) "\n" else ""

                typer.send(item.toChunk(columns) + suffix)
                delay(linePauseMs)

                if (isLastBlock) {
                    while (priorityLines.isNotEmpty()) {
                        val next = priorityLines.removeAt(0)
                        typer.send(next.toChunk(columns) + "\n")
                        delay(linePauseMs)
                    }
                }
            }
        }

        LaunchedEffect(columns) {
            typer.send("Initialization ")
            delay(initPauseMs)
            typer.send(".")
            delay(initPauseMs / 2)
            typer.send(".")
            delay(initPauseMs / 2)
            typer.send(".\n\n\n")

            vm.boot()

            val scriptItems = listOf(
                BootItem.Line("Voyager waking up ..."),
                BootItem.Row("Copilot", "READY"),
                BootItem.Line("Connection to Sisters of EVE ..."),
                BootItem.Line("Connection established successfully!")
            )

            enqueueItems(scriptItems, isLastBlock = false)

            typer.send("\n")
            delay(linePauseMs)

            val randomItems = scriptLinesRandom
                .shuffled()
                .take(randomCount)

            enqueueItems(randomItems, isLastBlock = true)

            typer.send("\n\n\nLaunching interface ... ")
            typer.close()
        }

        LaunchedEffect(Unit) {
            typer.run()
            vm.onTerminalDone()
        }

        Text(
            text = displayText,
            modifier = Modifier
                .fillMaxWidth()
                .blur(6.dp)
                .alpha(0.35f),
            style = terminalStyle
        )

        SelectionContainer {
            Text(
                text = displayText,
                modifier = Modifier.fillMaxWidth(),
                style = terminalStyle
            )
        }
    }
}
