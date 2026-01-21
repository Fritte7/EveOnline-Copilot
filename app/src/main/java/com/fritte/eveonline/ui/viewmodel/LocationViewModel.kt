package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.data.model.esi.CharacterLocation
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.data.repo.LocationRepository
import com.fritte.eveonline.data.repo.TokenStore
import com.fritte.eveonline.ui.states.UiState
import com.fritte.eveonline.utils.safeApiCall
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
    private val repo: LocationRepository,
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _onlineState = MutableStateFlow<UiState<CharacterLocationOnline>>(UiState.Idle)
    val onlineState: StateFlow<UiState<CharacterLocationOnline>> = _onlineState

    private val _locationState = MutableStateFlow<UiState<CharacterLocation>>(UiState.Idle)
    val locationState: StateFlow<UiState<CharacterLocation>> = _locationState

    private var onlineJob: Job? = null
    private var locationJob: Job? = null
    private val pollingLocationDelay = 15_000L
    private val pollingOnlineStateDelay = 60_000L

    init {
        viewModelScope.launch {
            tokenStore.characterIdFlow
                .distinctUntilChanged()
                .collect { characterId ->
                    characterId?.let(::startOnlinePolling) ?: stopPolling()
                }
        }
    }

    val characterName = tokenStore.characterNameFlow
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
                    repo.fetchCharacterLocationOnline(characterId)
                }
                _onlineState.value = onlineResult

                val isOnline = (onlineResult as? UiState.Success)?.data?.online == true

                if (isOnline) {
                    startLocationPolling(characterId)
                } else {
                    stopLocationPolling()
                }

                delay(pollingOnlineStateDelay)
            }
        }
    }

    private fun startLocationPolling(characterId: Long) {
        if (locationJob?.isActive == true) return

        locationJob = viewModelScope.launch {
            while (isActive) {

                _locationState.value = UiState.Loading
                _locationState.value = safeApiCall {
                    repo.fetchCharacterLocation(characterId)
                }

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