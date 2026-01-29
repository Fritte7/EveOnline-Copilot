package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import com.fritte.eveonline.domain.repository.LocationPoller
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LocationViewModel(
    tokenRepo: DataStoreTokenRepository,
    poller: LocationPoller
) : ViewModel() {

    val onlineState = poller.onlineState
    val locationUiState = poller.locationUiState

    val characterName = tokenRepo.characterNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}