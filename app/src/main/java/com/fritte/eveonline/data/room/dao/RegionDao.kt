package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.RegionEntity

@Dao
interface RegionDao {
    @Query("SELECT * FROM regionentity")
    fun getAllRegions(): List<RegionEntity>

    @Query("SELECT * FROM regionentity WHERE name = :name")
    fun getRegionByName(name: String): RegionEntity?

    @Insert
    suspend fun insertRegion(regionEntity: RegionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RegionEntity>)

    @Query("DELETE FROM regionentity")
    fun deleteAll()
}