package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.room.entities.SystemEntity
import kotlinx.coroutines.flow.Flow

interface WatchedSystemRepository {
    suspend fun getWatchedSystems(): Flow<List<SystemEntity>>
    suspend fun watch(systemId: Long): Boolean
    suspend fun unWatch(systemId: Long): Boolean
    suspend fun isWatched(systemId: Long): Boolean
}