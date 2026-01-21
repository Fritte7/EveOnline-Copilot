package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.RegionEntity

@Dao
interface RegionDao {
    @Query("SELECT * FROM region")
    suspend fun getAllRegions(): List<RegionEntity>

    @Query("SELECT * FROM region WHERE regionId = :id")
    suspend fun getRegionById(id: Long): RegionEntity?

    @Query("SELECT * FROM region WHERE name = :name")
    suspend fun getRegionByName(name: String): RegionEntity?

    @Insert
    suspend fun insertRegion(regionEntity: RegionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RegionEntity>)

    @Query("DELETE FROM region")
    fun deleteAll()
}