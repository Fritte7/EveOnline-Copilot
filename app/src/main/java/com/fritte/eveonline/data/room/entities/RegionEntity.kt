package com.fritte.eveonline.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "region",
    indices = [Index("name")]
)
data class RegionEntity(
    @PrimaryKey val regionId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "constellationsIds") val constellationsIds: List<Long>,
)