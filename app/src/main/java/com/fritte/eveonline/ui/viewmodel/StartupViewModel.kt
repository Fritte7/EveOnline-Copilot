package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.data.AnoikisImporter
import com.fritte.eveonline.ui.states.StartupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartupViewModel(
    private val anoikisImporter: AnoikisImporter
) : ViewModel() {

    private val _state = MutableStateFlow<StartupState>(StartupState.Booting)
    val state: StateFlow<StartupState> = _state

    fun boot() {
        viewModelScope.launch {
            try {
                _state.value = StartupState.Booting
                anoikisImporter.importIfNeeded()
                _state.value = StartupState.Ready
            } catch (t: Throwable) {
                _state.value = StartupState.Error(t.message ?: "Startup failed")
            }
        }
    }
}
