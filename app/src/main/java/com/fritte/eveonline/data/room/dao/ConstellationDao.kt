package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.Constellation

@Dao
interface ConstellationDao {
    @Query("SELECT * FROM constellation")
    fun getAllConstellations(): List<Constellation>

    @Query("SELECT * FROM constellation WHERE name = :name")
    fun getConstellationByName(name: String): Constellation?

    @Query("SELECT * FROM constellation WHERE region = :regionName")
    fun getConstellationsByRegion(regionName: Int): List<Constellation>

    @Insert
    suspend fun insertConstellation(constellation: Constellation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Constellation>)

    @Query("DELETE FROM constellation")
    fun deleteAll()
}