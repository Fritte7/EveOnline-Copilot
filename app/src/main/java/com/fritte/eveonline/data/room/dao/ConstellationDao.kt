package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.ConstellationEntity

@Dao
interface ConstellationDao {
    @Query("SELECT * FROM constellation")
    suspend fun getAllConstellations(): List<ConstellationEntity>

    @Query("SELECT * FROM constellation WHERE constellationId = :id")
    suspend fun getConstellationById(id: Long): ConstellationEntity?

    @Query("SELECT * FROM constellation WHERE name = :name")
    suspend fun getConstellationByName(name: String): ConstellationEntity?

    @Query("SELECT * FROM constellation WHERE regionId = :regionId")
    suspend fun getConstellationsByRegion(regionId: Long): List<ConstellationEntity>

    @Insert
    suspend fun insertConstellation(constellationEntity: ConstellationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ConstellationEntity>)

    @Query("DELETE FROM constellation")
    suspend fun deleteAll()
}