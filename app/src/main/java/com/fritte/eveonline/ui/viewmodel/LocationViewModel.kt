package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.data.model.esi.CharacterLocation
import com.fritte.eveonline.data.repo.LocationRepository
import com.fritte.eveonline.ui.states.UiState
import com.fritte.eveonline.utils.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(
    val repo: LocationRepository
): ViewModel() {

    private val _configState = MutableStateFlow<UiState<CharacterLocation>>(UiState.Idle)
    val locationState: StateFlow<UiState<CharacterLocation>> = _configState
    fun loadCurrentCharacterLocation(characterId: Long) {
        viewModelScope.launch {
            _configState.value = UiState.Loading

            _configState.value = safeApiCall { repo.fetchCharacterLocation(characterId) }
        }
    }
}