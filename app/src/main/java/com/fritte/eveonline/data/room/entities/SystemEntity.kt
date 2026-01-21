package com.fritte.eveonline.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "system",
    indices = [Index("name")]
)
data class SystemEntity(
    @PrimaryKey val systemId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "wormholeClass") val wormholeClass: String,
    @ColumnInfo(name = "effect") val effect: String?,
    @ColumnInfo(name = "statics") val statics: List<String>,
    @ColumnInfo(name = "constellationId") val constellationId: Long,
    @ColumnInfo(name = "alias") val alias: String?,
)