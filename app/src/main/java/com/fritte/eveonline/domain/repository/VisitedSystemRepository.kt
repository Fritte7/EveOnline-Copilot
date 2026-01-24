package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.room.entities.VisitedSystemEntity

interface VisitedSystemRepository {
    suspend fun getLastVisit() : VisitedSystemEntity?
    suspend fun getLastVisitForSystem(systemId: Long) : VisitedSystemEntity?
    suspend fun insertVisit(solarSystemId: Long, visitedAt: Long)
}