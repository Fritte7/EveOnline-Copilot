package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.ui.model.VisitTimelineRow

@Dao
interface VisitedSystemDao {

    @Insert
    suspend fun insertVisit(visit: VisitedSystemEntity)

    @Insert
    suspend fun insertVisits(visits: List<VisitedSystemEntity>)

    @Query("SELECT COUNT(*) FROM visited_system WHERE systemId = :systemId")
    suspend fun getVisitCount(systemId: Long): Int

    @Query("SELECT MAX(visitedAt) FROM visited_system WHERE systemId = :systemId")
    suspend fun getLastVisited(systemId: Long): Long?

    @Query(
        "SELECT v.visitedAt AS visitedAt, " +
        "v.systemId AS systemId, " +
        "s.name AS systemName," +
        "s.effect AS effect " +
        "FROM visited_system v " +
        "JOIN system s ON s.systemId = v.systemId " +
        "ORDER BY v.visitedAt DESC " +
        "LIMIT :limit OFFSET :offset"
    )
    suspend fun getTimeline(limit: Int, offset: Int): List<VisitTimelineRow>

    @Query("SELECT COUNT(DISTINCT systemId) FROM visited_system")
    suspend fun getUniqueSystemsVisited(): Int

    @Query("DELETE FROM visited_system")
    suspend fun clearAll()
}
