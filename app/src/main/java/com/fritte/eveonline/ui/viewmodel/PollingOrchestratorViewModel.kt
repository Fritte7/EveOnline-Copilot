package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import com.fritte.eveonline.domain.repository.ForegroundTracker
import com.fritte.eveonline.domain.repository.LocationPoller
import com.fritte.eveonline.domain.repository.StartupRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PollingOrchestratorViewModel(
    private val tokenRepo: DataStoreTokenRepository,
    private val startupRepo: StartupRepository,
    private val foregroundTracker: ForegroundTracker,
    private val locationPoller: LocationPoller
) : ViewModel() {

    init {
        viewModelScope.launch {
            combine(
                startupRepo.isReady,
                tokenRepo.characterIdFlow,
                foregroundTracker.isForeground
            ) { ready, characterId, fg ->
                Triple(ready, characterId, fg)
            }
                .distinctUntilChanged()
                .collect { (ready, characterId, fg) ->
                    val shouldPoll = ready && fg && characterId != null
                    if (shouldPoll) locationPoller.start(characterId)
                    else locationPoller.stop()
                }
        }
    }

    override fun onCleared() {
        locationPoller.stop()
        super.onCleared()
    }
}