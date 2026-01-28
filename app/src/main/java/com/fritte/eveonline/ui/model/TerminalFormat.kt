package com.fritte.eveonline.ui.model

sealed interface BootItem {
    data class Line(val text: String) : BootItem
    data class Row(val label: String, val value: String) : BootItem
    object BlankLine : BootItem
}

fun BootItem.toChunk(
    columns: Int,
    dotsMin: Int = 6
): String = when (this) {
    is BootItem.Line -> text
    BootItem.BlankLine -> ""
    is BootItem.Row -> {
        val spacer = " "
        val base = label + spacer
        val remaining = columns - base.length - value.length - spacer.length

        val dotsCount = remaining.coerceAtLeast(dotsMin)
        val dots = ".".repeat(dotsCount)

        base + dots + spacer + value
    }
}
