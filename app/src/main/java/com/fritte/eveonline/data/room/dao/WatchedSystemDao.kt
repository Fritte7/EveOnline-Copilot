package com.fritte.eveonline.data.room.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.SystemEntity
import com.fritte.eveonline.data.room.entities.WatchedSystemEntity
import kotlinx.coroutines.flow.Flow

interface WatchedSystemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun watch(entity: WatchedSystemEntity): Boolean

    @Query("DELETE FROM watched_system WHERE systemId = :systemId")
    suspend fun unwatch(systemId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM watched_system WHERE systemId = :systemId)")
    suspend fun isWatched(systemId: Long): Boolean

    @Query(
        "SELECT s.* " +
        "FROM system s " +
        "INNER JOIN watched_system w ON w.systemId = s.systemId " +
        "ORDER BY w.createdAt DESC"
    )
    fun getWatchedSystems(): Flow<List<SystemEntity>>
}