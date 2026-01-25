package com.fritte.eveonline.domain.usecase

import com.fritte.eveonline.data.room.entities.SystemEntity
import com.fritte.eveonline.domain.repository.WatchedSystemRepository
import kotlinx.coroutines.flow.Flow

class WatchSystemUseCase(
    private val repo: WatchedSystemRepository
) {
    suspend fun watch(systemId: Long): Boolean =
        repo.watch(systemId)

    suspend fun unwatch(systemId: Long): Boolean =
        repo.unWatch(systemId)

    suspend fun isWatched(systemId: Long): Boolean =
        repo.isWatched(systemId)

    suspend fun getWatchedSystems(): Flow<List<SystemEntity>> =
        repo.getWatchedSystems()
}