package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.dao.WatchedSystemDao
import com.fritte.eveonline.data.room.entities.SystemEntity
import com.fritte.eveonline.data.room.entities.WatchedSystemEntity
import com.fritte.eveonline.domain.repository.WatchedSystemRepository
import kotlinx.coroutines.flow.Flow

class WatchedSystemRepositoryImpl(
    private val dao : WatchedSystemDao,
) : WatchedSystemRepository {

    override suspend fun getWatchedSystems(): Flow<List<SystemEntity>> =
        dao.getWatchedSystems()

    override suspend fun watch(systemId: Long) =
        dao.watch(WatchedSystemEntity(systemId)) != -1L

    override suspend fun unWatch(systemId: Long): Boolean =
        dao.unwatch(systemId) > 0

    override suspend fun isWatched(systemId: Long): Boolean =
        dao.isWatched(systemId)
}