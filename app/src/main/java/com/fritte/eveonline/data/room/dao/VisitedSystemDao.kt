package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity

@Dao
interface VisitedSystemDao {

    @Insert
    suspend fun insertVisit(visit: VisitedSystemEntity)

    @Query("SELECT COUNT(*) FROM visited_system WHERE systemId = :systemId")
    suspend fun getVisitCount(systemId: Long): Int

    @Query("SELECT MAX(visitedAt) FROM visited_system WHERE systemId = :systemId")
    suspend fun getLastVisited(systemId: Long): String?

    @Query("SELECT COUNT(DISTINCT systemId) FROM visited_system")
    suspend fun getUniqueSystemsVisited(): Int

    @Query("DELETE FROM visited_system")
    suspend fun clearAll()
}
