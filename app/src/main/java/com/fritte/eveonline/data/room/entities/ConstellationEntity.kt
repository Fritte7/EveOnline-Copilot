package com.fritte.eveonline.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "constellation",
    indices = [Index("name")]
)
data class ConstellationEntity(
    @PrimaryKey val constellationId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "systemsIds") val systemsIds: List<Long>,
    @ColumnInfo(name = "regionId") val regionId: Long,
)