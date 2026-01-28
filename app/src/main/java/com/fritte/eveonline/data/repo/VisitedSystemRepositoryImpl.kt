package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.dao.VisitedSystemDao
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.domain.repository.VisitedSystemRepository
import com.fritte.eveonline.ui.model.VisitTimelineRow
import kotlinx.coroutines.flow.Flow

class VisitedSystemRepositoryImpl(
    private val dao: VisitedSystemDao,
): VisitedSystemRepository {

    override suspend fun getLastVisit(): VisitedSystemEntity? {
        return dao.getLastVisited()
    }

    override suspend fun getLastVisitForSystem(systemId: Long): VisitedSystemEntity? {
        return dao.getLastVisited(systemId)
    }

    override suspend fun insertVisit(solarSystemId: Long, visitedAt: Long) {
        dao.insertVisit(
            VisitedSystemEntity(
                systemId = solarSystemId,
                visitedAt = visitedAt
            )
        )
    }

    override fun observeTimeline(limit: Int): Flow<List<VisitTimelineRow>> {
        return dao.getTimeline(limit)
    }
}