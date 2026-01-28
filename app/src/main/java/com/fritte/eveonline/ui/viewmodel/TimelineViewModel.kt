package com.fritte.eveonline.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fritte.eveonline.domain.usecase.GetTimelineUseCase
import com.fritte.eveonline.ui.model.VisitTimelineRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TimelineViewModel(
    timelineUseCase: GetTimelineUseCase
) : ViewModel() {

    val timeline: StateFlow<List<VisitTimelineRow>> =
        timelineUseCase(limit = 300)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _selectedSystemId = MutableStateFlow<Long?>(null)
    val selectedSystemId: StateFlow<Long?> = _selectedSystemId

    fun onSelectSystem(systemId: Long) {
        _selectedSystemId.value = systemId
    }

    fun clearTimeline() {
        TODO("Not yet implemented")
    }

    fun setLimit(limit: Int) {
        TODO("Not yet implemented")
    }
}
