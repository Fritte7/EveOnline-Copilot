package com.fritte.eveonline.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ForegroundTracker {
    val isForeground: StateFlow<Boolean>
    fun setForeground(isForeground: Boolean)
}