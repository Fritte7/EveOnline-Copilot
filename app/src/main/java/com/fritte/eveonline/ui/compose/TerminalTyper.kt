package com.fritte.eveonline.ui.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Stable
class TerminalTyper internal constructor(
    private val glitchChars: List<Char>,
    private val ghostChars: List<Char>,
    private val glitchDelayMs: Long,
    private val ghostDelayMs: Long,
    private val glitchChance: Float,
    private val ghostChance: Float,
    private val charDelayMs: Long,
) {
    private val queue = Channel<String>(capacity = Channel.BUFFERED)

    var typedText by mutableStateOf("")
        private set

    var isDone by mutableStateOf(false)
        private set

    suspend fun send(chunk: String) = queue.send(chunk)
    fun close() = queue.close()

    suspend fun run() {
        for (chunk in queue) {
            typeChunk(chunk)
        }
        isDone = true
    }

    private suspend fun typeChunk(chunk: String) {
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
}

@Composable
fun rememberTerminalTyper(
    glitchChars: List<Char> = listOf('#', '%', '@', '?', '*', '&'),
    ghostChars: List<Char> = listOf('_', '░'),
    glitchDelayMs: Long = 45L,
    ghostDelayMs: Long = 60L,
    glitchChance: Float = 0.07f,
    ghostChance: Float = 0.07f,
    charDelayMs: Long = 18L,
): TerminalTyper {
    return remember {
        TerminalTyper(
            glitchChars = glitchChars,
            ghostChars = ghostChars,
            glitchDelayMs = glitchDelayMs,
            ghostDelayMs = ghostDelayMs,
            glitchChance = glitchChance,
            ghostChance = ghostChance,
            charDelayMs = charDelayMs
        )
    }
}

@Composable
fun typedTextWithCursor(typedText: String, isDone: Boolean): String {
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

    val cursorVisible = !isDone && cursorAlpha > 0.5f
    return if (isDone) typedText else typedText + if (cursorVisible) "|" else " "
}