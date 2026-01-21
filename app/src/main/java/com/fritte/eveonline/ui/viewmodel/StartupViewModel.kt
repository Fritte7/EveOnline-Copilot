package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.domain.repository.AnoikisImporterRepository
import com.fritte.eveonline.ui.states.StartupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartupViewModel(
    private val anoikisImporterRepository: AnoikisImporterRepository
) : ViewModel() {

    private val _state = MutableStateFlow<StartupState>(StartupState.Booting)
    val state: StateFlow<StartupState> = _state

    fun boot() {
        viewModelScope.launch {
            try {
                _state.value = StartupState.Booting
                anoikisImporterRepository.importIfNeeded()
                _state.value = StartupState.ImportingStaticData
            } catch (t: Throwable) {
                _state.value = StartupState.Error(t.message ?: "Startup failed")
            }
        }
    }

    fun onTerminalDone() {
        _state.value = StartupState.TerminalDone
    }

    fun ready() {
        _state.value = StartupState.Ready
    }
}
