package com.fritte.eveonline.domain.usecase

import com.fritte.eveonline.domain.repository.VisitedSystemRepository
import com.fritte.eveonline.ui.model.VisitTimelineRow
import kotlinx.coroutines.flow.Flow

class GetTimelineUseCase(
    private val visitedRepo: VisitedSystemRepository
) {

    operator fun invoke(limit: Int): Flow<List<VisitTimelineRow>> {
        return visitedRepo.observeTimeline(limit)
    }
}