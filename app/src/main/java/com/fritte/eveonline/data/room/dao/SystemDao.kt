package com.fritte.eveonline.data.room.dao

import com.fritte.eveonline.data.room.entities.SystemEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SystemDao {
    @Query("SELECT COUNT(*) FROM system")
    suspend fun countSystems(): Int

    @Query("SELECT * FROM system")
    suspend fun getAll(): List<SystemEntity>

    @Query("SELECT * FROM system WHERE systemId = :id")
    suspend fun getSystemById(id: Long): SystemEntity?

    @Query("SELECT * FROM system WHERE name = :key OR alias = :key LIMIT 1")
    suspend fun getSystemByName(key: String): SystemEntity?

    @Query("SELECT * FROM system WHERE constellationId = :constellationId")
    suspend fun getSystemsByConstellation(constellationId: Long): List<SystemEntity>

    @Insert
    suspend fun insert(systemEntity: SystemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SystemEntity>)

    @Query("DELETE FROM system")
    suspend fun deleteAll()
}