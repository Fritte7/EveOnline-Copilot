package com.fritte.eveonline.ui.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fritte.eveonline.theme.EveColors
import com.fritte.eveonline.ui.states.StartupState
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun BootScreen(
    modifier: Modifier = Modifier,
    vm: StartupViewModel
) {
    val state by vm.state.collectAsState()

    val initLine          = "Initialization "
    val staticData        = "Importing static data ........... OK"
    val visitedSystemData = "Checking visited system ......... OK"
    val checkingAuth      = "Checking authentication ......... OK"
    val endLine    = "\n\n\nLaunching interface ................ "
    val priorityLines = remember { mutableStateListOf<String>() }
    val scriptLines   = remember {
        listOf(
            "Voyager waking up ... ",
            "Copilot ...................... READY",
            "Connection to Sisters of EVE ...... ",
            "Connection established successfully!\n",
        )
    }
    val scriptLinesRandom = remember {
        listOf(
            "Turning lights ON ............... OK",
            "Neural link stabilization ....... OK",
            "Handshake: CONCORD relay ........ OK",
            "Calibrating probe launcher ...... OK",
            "Loading probes .................. OK",
            "Starting scanner ................ OK",
            "Initializing coffee injector .... OK",
            "Caffeine reserves ......... CRITICAL",
            "Gravimetric sensors online ...... OK",
            "Cloaking device standby ......... OK",
            "Covert ops protocols loaded ..... OK",
            "Warp core integrity ......... STABLE",
            "Loading Anoikis topology ........ OK",
            "Cleaning sensors ................ OK",
            "Decrypting bookmarks ............ OK",
            "Injecting: \"Stay Neutral.\" ...... OK",
            "Towel inventory ............ PRESENT",
        )
    }

    // Glitch config (sane defaults)
    val glitchChars = listOf('#', '%', '@', '?', '*', '&')
    val ghostChars = listOf('_', '░')
    val glitchDelayMs = 45L
    val ghostDelayMs = 60L
    val glitchChance = 0.07f   // 7%
    val ghostChance = 0.07f   // 7%

    val randomCount = 7
    val charDelayMs = 18L
    val linePauseMs = 300L
    val initPauseMs = 1000L

    var typedText by remember { mutableStateOf("") }
    var isDone by remember { mutableStateOf(false) }

    // Queue of chunks to type (single writer consumes this)
    val queue = remember { Channel<String>(capacity = Channel.BUFFERED) }
    // Cursor blink (we will append it INSIDE the text so it follows lines)
    val infinite = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    suspend fun enqueueLinesWithPriority(
        lines: List<String>,
        isLastBlock: Boolean
    ) {
        lines.forEachIndexed { index, line ->
            val isLastLine = isLastBlock && index == lines.lastIndex
            val suffix = if (!isLastLine) "\n" else ""

            queue.send(line + suffix)
            delay(linePauseMs)

            // Insert any status updates at safe boundaries
            if (isLastBlock) {
                while (priorityLines.isNotEmpty()) {
                    val next = priorityLines.removeAt(0)
                    queue.send(next + "\n")
                    delay(linePauseMs)
                }
            }
        }
    }
    suspend fun typeChunk(chunk: String) {
        for (ch in chunk) {
            if (Random.nextFloat() < ghostChance) {
                typedText += ghostChars.random()
                delay(ghostDelayMs)
                typedText = typedText.dropLast(1)
            }

            typedText += ch
            delay(charDelayMs)

            if (Random.nextFloat() < glitchChance && ch != '\n' && ch != ' ') {
                val correctChar = typedText.last()
                val glitchChar = glitchChars.random()
                typedText = typedText.dropLast(1) + glitchChar
                delay(glitchDelayMs)
                typedText = typedText.dropLast(1) + correctChar
            }
        }
    }

    LaunchedEffect(Unit) {
        queue.send(initLine)
        delay(initPauseMs)
        queue.send(".")
        delay(initPauseMs / 2)
        queue.send(".")
        delay(initPauseMs / 5)
        queue.send(".\n\n\n")
        vm.boot()

        enqueueLinesWithPriority(
            lines = scriptLines,
            isLastBlock = false
        )
        val randomLines = scriptLinesRandom.shuffled().take(randomCount)
        queue.send("\n")
        delay(linePauseMs)

        enqueueLinesWithPriority(
            lines = randomLines,
            isLastBlock = true
        )
        // Run last line
        queue.send(endLine)
        delay(linePauseMs)
        queue.close()
    }
    LaunchedEffect(state) {
        when (state) {
            StartupState.ImportingStaticData ->
                priorityLines.add(staticData)
            StartupState.ImportingVisitedSystemData ->
                priorityLines.add(visitedSystemData)
            StartupState.CheckingAuth ->
                priorityLines.add(checkingAuth)
            is StartupState.Error ->
                priorityLines.add("ERROR: ${(state as StartupState.Error).message} !!!!")
            else -> Unit
        }
    }
    LaunchedEffect(Unit) {
        for (chunk in queue) {
            typeChunk(chunk)
        }
        isDone = true
        vm.onTerminalDone()
    }

    // Build text with blinking cursor INSIDE it (so it follows newlines)
    val cursorVisible = !isDone && cursorAlpha > 0.5f
    val displayText = if (isDone) {
        typedText
    } else {
        typedText + if (cursorVisible) "|" else " " // space avoids layout jitter
    }

    // Terminal styling
    val green = EveColors.Primary
    val terminalStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 16.sp,
        color = green
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
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

        ScanlineOverlay(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.12f)
        )
    }
}

@Composable
private fun ScanlineOverlay(modifier: Modifier = Modifier) {
    val lineColor = EveColors.Scanline
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.00f to EveColors.Transparent,
                    0.02f to lineColor,
                    0.04f to EveColors.Transparent,
                    0.10f to EveColors.Transparent
                )
            )
        )
    )
}
