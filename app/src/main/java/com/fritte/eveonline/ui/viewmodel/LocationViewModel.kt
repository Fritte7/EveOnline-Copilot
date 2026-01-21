package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.ui.model.LocationUI
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import com.fritte.eveonline.domain.usecase.GetLocationUIUseCase
import com.fritte.eveonline.domain.usecase.GetOnlineStatusUseCase
import com.fritte.eveonline.ui.states.UiState
import com.fritte.eveonline.data.network.safeApiCall
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LocationViewModel(
    private val dataStoreTokenRepository: DataStoreTokenRepository,
    private val getOnlineStatusUseCase: GetOnlineStatusUseCase,
    private val getLocationUiUseCase: GetLocationUIUseCase,
) : ViewModel() {

    private val _onlineState = MutableStateFlow<UiState<CharacterLocationOnline>>(UiState.Idle)
    val onlineState: StateFlow<UiState<CharacterLocationOnline>> = _onlineState

    private val _locationUiState = MutableStateFlow<UiState<LocationUI>>(UiState.Idle)
    val locationUiState: StateFlow<UiState<LocationUI>> = _locationUiState

    private var onlineJob: Job? = null
    private var locationJob: Job? = null
    private val pollingLocationDelay = 15_000L
    private val pollingOnlineStateDelay = 60_000L

    init {
        viewModelScope.launch {
            dataStoreTokenRepository.characterIdFlow
                .distinctUntilChanged()
                .collect { characterId ->
                    characterId?.let(::startOnlinePolling) ?: stopPolling()
                }
        }
    }

    val characterName = dataStoreTokenRepository.characterNameFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private fun startOnlinePolling(characterId: Long) {
        if (onlineJob?.isActive == true) return

        onlineJob = viewModelScope.launch {
            while (isActive) {

                _onlineState.value = UiState.Loading
                val onlineResult = safeApiCall {
                    getOnlineStatusUseCase(characterId)
                }
                _onlineState.value = onlineResult

                val isOnline = (onlineResult as? UiState.Success)?.data?.online == true

                if (isOnline) {
                    startLocationPolling(characterId)
                } else {
                    stopLocationPolling()
                    refreshLastKnownLocationOnce(characterId)
                }

                delay(pollingOnlineStateDelay)
            }
        }
    }

    private fun refreshLastKnownLocationOnce(characterId: Long) {
        viewModelScope.launch {
            val uiResult = safeApiCall { getLocationUiUseCase(characterId, isOnline = false) }

            if (uiResult is UiState.Success) {
                _locationUiState.value = uiResult
            } else {
                if (_locationUiState.value !is UiState.Success) {
                    _locationUiState.value = uiResult
                }
            }
        }
    }


    private fun startLocationPolling(characterId: Long) {
        if (locationJob?.isActive == true) return

        locationJob = viewModelScope.launch {
            while (isActive) {
                _locationUiState.value = UiState.Loading

                val uiResult = safeApiCall { getLocationUiUseCase(characterId, isOnline = true) }
                _locationUiState.value = uiResult

                delay(pollingLocationDelay)
            }
        }
    }


    private fun stopLocationPolling() {
        locationJob?.cancel()
        locationJob = null
    }

    fun stopPolling() {
        onlineJob?.cancel()
        locationJob?.cancel()
        onlineJob = null
        locationJob = null
    }

    override fun onCleared() {
        stopPolling()
        super.onCleared()
    }
}