package com.fritte.eveonline.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fritte.eveonline.data.room.entities.Region

@Dao
interface RegionDao {
    @Query("SELECT * FROM region")
    fun getAllRegions(): List<Region>

    @Query("SELECT * FROM region WHERE name = :name")
    fun getRegionByName(name: String): Region?

    @Insert
    suspend fun insertRegion(region: Region)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Region>)

    @Query("DELETE FROM region")
    fun deleteAll()
}