package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.fritte.eveonline.ui.nav.Cmd
import com.fritte.eveonline.ui.nav.CommandEvent
import com.fritte.eveonline.ui.nav.NavEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationViewModel : ViewModel() {

    private val _nav = MutableSharedFlow<NavEvent>(extraBufferCapacity = 32)
    val nav = _nav.asSharedFlow()

    private val _cmd = MutableSharedFlow<CommandEvent>(extraBufferCapacity = 64)
    val cmd = _cmd.asSharedFlow()

    fun emitNav(ev: NavEvent) { _nav.tryEmit(ev) }
    fun emitCmd(ev: CommandEvent) { _cmd.tryEmit(ev) }

    fun submitLine(line: String) {
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return

        // Always broadcast raw (useful for logging)
        _cmd.tryEmit(CommandEvent.Raw(trimmed))

        parseAndDispatch(trimmed)
    }

    private fun parseAndDispatch(line: String) {
        val parts = line.split(Regex("\\s+"))

        when (parts.firstOrNull()?.lowercase()) {
            Cmd.CD -> when (parts.getOrNull(1)?.lowercase()) {
                Cmd.NAV      -> emitNav(NavEvent.ToMain)
                Cmd.TIM       -> emitNav(NavEvent.ToTim)
                Cmd.DB        -> emitNav(NavEvent.ToDb)
                Cmd.CFG       -> emitNav(NavEvent.ToCfg)
                Cmd.DUBLE_DOT -> emitNav(NavEvent.Back)
            }

            Cmd.TIMELINE -> when (parts.getOrNull(1)?.lowercase()) {
                Cmd.CLEAR -> emitCmd(CommandEvent.TimelineClear)
                Cmd.LIMIT -> parts.getOrNull(2)?.toIntOrNull()?.let { emitCmd(CommandEvent.TimelineLimit(it)) }
            }

            Cmd.QUERY -> emitCmd(CommandEvent.DbQuery(parts.drop(1).joinToString(" ")))

            Cmd.OPEN -> {
                if (parts.getOrNull(1)?.lowercase() == "system") {
                    parts.getOrNull(2)?.toLongOrNull()?.let { emitNav(NavEvent.ToSystemDetails(it)) }
                }
            }
        }
    }
}