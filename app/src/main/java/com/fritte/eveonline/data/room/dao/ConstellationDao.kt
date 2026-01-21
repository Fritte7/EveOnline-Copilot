package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.ConstellationEntity

@Dao
interface ConstellationDao {
    @Query("SELECT * FROM constellation")
    fun getAllConstellations(): List<ConstellationEntity>

    @Query("SELECT * FROM constellation WHERE constellationId = :id")
    fun getConstellationById(id: Long): ConstellationEntity?

    @Query("SELECT * FROM constellation WHERE name = :name")
    fun getConstellationByName(name: String): ConstellationEntity?

    @Query("SELECT * FROM constellation WHERE regionId = :regionId")
    fun getConstellationsByRegion(regionId: Long): List<ConstellationEntity>

    @Insert
    suspend fun insertConstellation(constellationEntity: ConstellationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ConstellationEntity>)

    @Query("DELETE FROM constellation")
    fun deleteAll()
}