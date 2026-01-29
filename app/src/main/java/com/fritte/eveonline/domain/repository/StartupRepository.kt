package com.fritte.eveonline.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface StartupRepository {
    val isReady: StateFlow<Boolean>
    fun setReady(ready: Boolean)
}