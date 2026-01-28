package com.fritte.eveonline.ui.nav

sealed interface CommandEvent {
    data class Raw(val line: String) : CommandEvent

    // Timeline
    data object TimelineClear : CommandEvent
    data class TimelineLimit(val limit: Int) : CommandEvent

    // DB
    data class DbQuery(val text: String) : CommandEvent

    // Watchlist
    data class WatchAdd(val systemIdOrName: String) : CommandEvent
    data class WatchRemove(val systemIdOrName: String) : CommandEvent
}
