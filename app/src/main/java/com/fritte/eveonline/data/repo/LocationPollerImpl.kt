package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.data.network.safeApiCall
import com.fritte.eveonline.domain.repository.LocationPoller
import com.fritte.eveonline.domain.usecase.GetLocationUIUseCase
import com.fritte.eveonline.domain.usecase.GetOnlineStatusUseCase
import com.fritte.eveonline.ui.model.LocationUI
import com.fritte.eveonline.ui.states.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationPollerImpl(
    private val scope: CoroutineScope,
    private val getOnlineStatus: GetOnlineStatusUseCase,
    private val getLocationUi: GetLocationUIUseCase,
) : LocationPoller {

    private val _onlineState = MutableStateFlow<UiState<CharacterLocationOnline>>(UiState.Idle)
    override val onlineState: StateFlow<UiState<CharacterLocationOnline>> = _onlineState

    private val _locationUiState = MutableStateFlow<UiState<LocationUI>>(UiState.Idle)
    override val locationUiState: StateFlow<UiState<LocationUI>> = _locationUiState

    private var onlineJob: Job? = null
    private var locationJob: Job? = null
    private var currentCharacterId: Long? = null

    private val pollingLocationDelay = 10_000L
    private val pollingOnlineDelay = 60_000L

    override fun start(characterId: Long) {
        if (currentCharacterId == characterId && onlineJob?.isActive == true) return

        stop()
        currentCharacterId = characterId

        onlineJob = scope.launch {
            while (isActive) {
                _onlineState.value = UiState.Loading
                val onlineResult = safeApiCall { getOnlineStatus(characterId) }
                _onlineState.value = onlineResult

                val isOnline = (onlineResult as? UiState.Success)?.data?.online == true
                if (isOnline) {
                    startLocationPolling(characterId)
                } else {
                    stopLocationPolling()
                    refreshLastKnownLocationOnce(characterId)
                }

                delay(pollingOnlineDelay)
            }
        }
    }

    private fun startLocationPolling(characterId: Long) {
        if (locationJob?.isActive == true) return

        locationJob = scope.launch {
            while (isActive) {
                _locationUiState.value = UiState.Loading
                val uiResult = safeApiCall { getLocationUi(characterId, isOnline = true) }
                _locationUiState.value = uiResult
                delay(pollingLocationDelay)
            }
        }
    }

    private fun refreshLastKnownLocationOnce(characterId: Long) {
        scope.launch {
            val uiResult = safeApiCall { getLocationUi(characterId, isOnline = false) }
            if (uiResult is UiState.Success) {
                _locationUiState.value = uiResult
            } else if (_locationUiState.value !is UiState.Success) {
                _locationUiState.value = uiResult
            }
        }
    }

    private fun stopLocationPolling() {
        locationJob?.cancel()
        locationJob = null
    }

    override fun stop() {
        onlineJob?.cancel()
        locationJob?.cancel()
        onlineJob = null
        locationJob = null
        currentCharacterId = null
    }
}