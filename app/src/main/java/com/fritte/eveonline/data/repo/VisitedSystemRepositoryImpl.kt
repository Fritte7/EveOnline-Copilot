package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.domain.repository.VisitedSystemRepository

class VisitedSystemRepositoryImpl(
    private val db: AppDatabase,
): VisitedSystemRepository {

    override suspend fun getLastVisit(): VisitedSystemEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getLastVisitForSystem(systemId: Long): VisitedSystemEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun insertVisit(solarSystemId: Long, visitedAt: Long) {
        TODO("Not yet implemented")
    }
}