package com.fritte.eveonline.domain.usecase

import com.fritte.eveonline.domain.repository.SystemRepository
import com.fritte.eveonline.domain.repository.VisitedSystemRepository
import kotlin.time.Clock

/* Record a system visit and return if it's a new one */
class RecordSystemVisitUseCase(
    private val visitedRepo: VisitedSystemRepository,
    private val systemRepo: SystemRepository,
) {

    private val minIntervalMs: Long = 24 * 60 * 60 * 1000L

    suspend operator fun invoke(solarSystemId: Long) : Boolean {
        // 1) only j-space
        if (!systemRepo.isJSpace(solarSystemId)) return false

        // 2) same as last => skip
        val last = visitedRepo.getLastVisit()
        if (last?.systemId == solarSystemId) return false

        val clock = Clock.System
        val now = clock.now().toEpochMilliseconds()

        // 3) cooldown for this system
        val lastVisitThisSystem = visitedRepo.getLastVisitForSystem(solarSystemId)
        if (lastVisitThisSystem != null && now - lastVisitThisSystem.visitedAt < minIntervalMs) return false

        visitedRepo.insertVisit(solarSystemId, now)

        return lastVisitThisSystem == null
    }
}