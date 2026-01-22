package com.fritte.eveonline.ui.states

sealed interface StartupState {
    data object Booting : StartupState
    data object ImportingStaticData : StartupState
    data object ImportingVisitedSystemData : StartupState
    data object CheckingAuth : StartupState
    data object Ready : StartupState
    data object TerminalDone : StartupState
    data class Error(val message: String) : StartupState
}