package com.fritte.eveonline.data.repo

import com.fritte.eveonline.domain.repository.ForegroundTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForegroundTrackerImpl : ForegroundTracker {
    private val _isForeground = MutableStateFlow(false)
    override val isForeground: StateFlow<Boolean> = _isForeground

    override fun setForeground(isForeground: Boolean) {
        _isForeground.value = isForeground
    }
}