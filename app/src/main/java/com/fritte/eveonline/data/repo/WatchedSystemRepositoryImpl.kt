package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.SystemEntity
import com.fritte.eveonline.data.room.entities.WatchedSystemEntity
import com.fritte.eveonline.domain.repository.WatchedSystemRepository
import kotlinx.coroutines.flow.Flow

class WatchedSystemRepositoryImpl(
    private val db: AppDatabase
) : WatchedSystemRepository {

    override suspend fun getWatchedSystems(): Flow<List<SystemEntity>> =
        db.watchedSystemDao().getWatchedSystems()

    override suspend fun watch(systemId: Long) =
        db.watchedSystemDao().watch(WatchedSystemEntity(systemId))

    override suspend fun unWatch(systemId: Long): Boolean =
        db.watchedSystemDao().unwatch(systemId)

    override suspend fun isWatched(systemId: Long): Boolean =
        db.watchedSystemDao().isWatched(systemId)
}