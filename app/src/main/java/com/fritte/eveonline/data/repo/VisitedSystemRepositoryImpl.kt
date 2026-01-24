package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.domain.repository.VisitedSystemRepository

class VisitedSystemRepositoryImpl(
    private val db: AppDatabase,
): VisitedSystemRepository {

    override suspend fun getLastVisit(): VisitedSystemEntity? {
        return db.visitedSystemDao().getLastVisited()
    }

    override suspend fun getLastVisitForSystem(systemId: Long): VisitedSystemEntity? {
        return db.visitedSystemDao().getLastVisited(systemId)
    }

    override suspend fun insertVisit(solarSystemId: Long, visitedAt: Long) {
        db.visitedSystemDao().insertVisit(
            VisitedSystemEntity(
                systemId = solarSystemId,
                visitedAt = visitedAt
            )
        )
    }
}