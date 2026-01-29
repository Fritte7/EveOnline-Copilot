package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.domain.repository.AnoikisImporterRepository
import com.fritte.eveonline.domain.repository.StartupRepository
import com.fritte.eveonline.domain.repository.VisitedSystemHistoryImporterRepository
import com.fritte.eveonline.ui.states.StartupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartupViewModel(
    private val startupRepository: StartupRepository,
    private val anoikisImporterRepository: AnoikisImporterRepository,
    private val visitedSystemHistoryImporterRepository: VisitedSystemHistoryImporterRepository
) : ViewModel() {

    private val _state = MutableStateFlow<StartupState>(StartupState.Booting)
    val state: StateFlow<StartupState> = _state
    private val _dataReady = MutableStateFlow(false)
    val dataReady: StateFlow<Boolean> = _dataReady
    private val _terminalDone = MutableStateFlow(false)
    val terminalDone: StateFlow<Boolean> = _terminalDone

    fun boot() {
        viewModelScope.launch {
            try {
                startupRepository.setReady(false)

                _state.value = StartupState.Booting

                _state.value = StartupState.ImportingStaticData

                anoikisImporterRepository.importIfNeeded()

                _state.value = StartupState.ImportingVisitedSystemData

                //visitedSystemHistoryImporterRepository.import()

                _dataReady.value = true
                startupRepository.setReady(true)
                _state.value = StartupState.Ready
            } catch (t: Throwable) {
                _dataReady.value = false
                _terminalDone.value = false
                startupRepository.setReady(false)
                _state.value = StartupState.Error(t.message ?: "Startup failed")
            }
        }
    }

    fun onTerminalDone() {
        _terminalDone.value = true
        _state.value = StartupState.TerminalDone
    }
}
