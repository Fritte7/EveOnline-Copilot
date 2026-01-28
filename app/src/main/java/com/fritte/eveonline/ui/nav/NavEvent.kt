package com.fritte.eveonline.ui.nav

sealed interface NavEvent {
    data object Back : NavEvent
    data object ToMain : NavEvent
    data object ToTim : NavEvent
    data object ToDb : NavEvent
    data object ToCfg : NavEvent
    data object ToSta : NavEvent
    data class ToSystemDetails(val systemId: Long) : NavEvent
}