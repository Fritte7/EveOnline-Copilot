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
    fun getAll(): List<SystemEntity>

    @Query("SELECT * FROM system WHERE name = :name")
    fun getSystemByName(name: String): SystemEntity?

    @Query("SELECT * FROM system WHERE constellation = :constellationName")
    fun getSystemsByConstellation(constellationName: String): List<SystemEntity>

    @Insert
    fun insert(systemEntity: SystemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SystemEntity>)

    @Query("DELETE FROM system")
    fun deleteAll()
}