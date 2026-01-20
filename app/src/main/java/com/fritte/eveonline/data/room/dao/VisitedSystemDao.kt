package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity

@Dao
interface VisitedSystemDao {
    @Query("SELECT * FROM visited_system ORDER BY visitedAt DESC")
    suspend fun getAllVisitedSystems(): List<VisitedSystemEntity>

    @Query("SELECT * FROM visited_system WHERE systemName = :systemName")
    suspend fun getSystem(systemName: String): VisitedSystemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSystem(system: VisitedSystemEntity)

    @Query("DELETE FROM visited_system")
    suspend fun clearAll()
}



