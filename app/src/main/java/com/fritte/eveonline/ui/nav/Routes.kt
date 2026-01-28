package com.fritte.eveonline.ui.nav

object Routes {
    const val BOOT  = "boot"
    const val LOGIN = "login"
    const val MAIN  = "main"
    const val TIM   = "timeline"
    const val CFG   = "config"
    const val DB    = "database"
    const val STA   = "stats"
    const val SYSTEM_DETAILS = "system/{systemId}"

    fun systemDetails(systemId: Long) = "system/$systemId"
}