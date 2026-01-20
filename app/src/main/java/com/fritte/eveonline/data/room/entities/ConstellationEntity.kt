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
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "systems") val systems: List<String>,
    @ColumnInfo(name = "region") val region: String,
)