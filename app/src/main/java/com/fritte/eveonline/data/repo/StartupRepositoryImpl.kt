package com.fritte.eveonline.data.repo

import com.fritte.eveonline.domain.repository.StartupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StartupRepositoryImpl : StartupRepository {
    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    override fun setReady(ready: Boolean) {
        _isReady.value = ready
    }
}