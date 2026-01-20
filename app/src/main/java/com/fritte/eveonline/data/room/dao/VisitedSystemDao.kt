package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity

@Dao
interface VisitedSystemDao {

    @Query("SELECT * FROM visited_system ORDER BY visitedAt DESC")
    suspend fun getAllVisits(): List<VisitedSystemEntity>

    @Query("SELECT * FROM visited_system WHERE systemName = :systemName ORDER BY visitedAt DESC")
    suspend fun getVisitsForSystem(systemName: String): List<VisitedSystemEntity>

    @Insert
    suspend fun insertVisit(visit: VisitedSystemEntity)

    @Query("SELECT COUNT(*) FROM visited_system WHERE systemName = :systemName")
    suspend fun getVisitCount(systemName: String): Int

    @Query("SELECT MAX(visitedAt) FROM visited_system WHERE systemName = :systemName")
    suspend fun getLastVisited(systemName: String): String?

    @Query("DELETE FROM visited_system")
    suspend fun clearAll()
}




