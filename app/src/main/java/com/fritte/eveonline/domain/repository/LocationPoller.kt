package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.ui.model.LocationUI
import com.fritte.eveonline.ui.states.UiState
import kotlinx.coroutines.flow.StateFlow

interface LocationPoller {
    val onlineState: StateFlow<UiState<CharacterLocationOnline>>
    val locationUiState: StateFlow<UiState<LocationUI>>

    fun start(characterId: Long)
    fun stop()
}