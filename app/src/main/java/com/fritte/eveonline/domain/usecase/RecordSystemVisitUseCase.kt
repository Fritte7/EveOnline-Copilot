package com.fritte.eveonline.domain.usecase

import com.fritte.eveonline.domain.repository.SystemRepository
import com.fritte.eveonline.domain.repository.VisitedSystemRepository
import kotlin.time.Clock

class RecordSystemVisitUseCase(
    private val visitedRepo: VisitedSystemRepository,
    private val systemRepo: SystemRepository,
) {

    private val minIntervalMs: Long = 24 * 60 * 60 * 1000L

    suspend operator fun invoke(solarSystemId: Long) {
        // 1) only j-space
        if (!systemRepo.isJSpace(solarSystemId)) return

        // 2) same as last => skip
        val last = visitedRepo.getLastVisit()
        if (last?.systemId == solarSystemId) return

        val clock = Clock.System
        val now = clock.now().toEpochMilliseconds()

        // 3) cooldown for this system
        val lastVisitThisSystem = visitedRepo.getLastVisitForSystem(solarSystemId)
        if (lastVisitThisSystem != null && now - lastVisitThisSystem.visitedAt < minIntervalMs) return

        visitedRepo.insertVisit(solarSystemId, now)
    }
}