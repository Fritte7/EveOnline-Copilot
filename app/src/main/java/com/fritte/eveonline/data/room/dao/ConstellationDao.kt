package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.ConstellationEntity

@Dao
interface ConstellationDao {
    @Query("SELECT * FROM constellationentity")
    fun getAllConstellations(): List<ConstellationEntity>

    @Query("SELECT * FROM constellationentity WHERE name = :name")
    fun getConstellationByName(name: String): ConstellationEntity?

    @Query("SELECT * FROM constellationentity WHERE region = :regionName")
    fun getConstellationsByRegion(regionName: Int): List<ConstellationEntity>

    @Insert
    suspend fun insertConstellation(constellationEntity: ConstellationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ConstellationEntity>)

    @Query("DELETE FROM constellationentity")
    fun deleteAll()
}