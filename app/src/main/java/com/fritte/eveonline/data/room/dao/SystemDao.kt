package com.fritte.eveonline.data.room.dao

import com.fritte.eveonline.data.room.entities.System
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SystemDao {
    @Query("SELECT COUNT(*) FROM system")
    suspend fun countSystems(): Int

    @Query("SELECT * FROM system")
    fun getAll(): List<System>

    @Query("SELECT * FROM system WHERE name = :name")
    fun getSystemByName(name: String): System?

    @Query("SELECT * FROM system WHERE constellation = :constellationName")
    fun getSystemsByConstellation(constellationName: String): List<System>

    @Insert
    fun insert(system: System)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<System>)

    @Query("DELETE FROM system")
    fun deleteAll()
}