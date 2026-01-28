package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.ui.model.VisitTimelineRow
import kotlinx.coroutines.flow.Flow

interface VisitedSystemRepository {
    suspend fun getLastVisit() : VisitedSystemEntity?
    suspend fun getLastVisitForSystem(systemId: Long) : VisitedSystemEntity?
    suspend fun insertVisit(solarSystemId: Long, visitedAt: Long)
    fun observeTimeline(limit: Int = 200): Flow<List<VisitTimelineRow>>
}