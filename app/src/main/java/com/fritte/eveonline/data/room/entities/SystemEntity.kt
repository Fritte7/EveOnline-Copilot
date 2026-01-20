package com.fritte.eveonline.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SystemEntity(
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "wormholeClass") val wormholeClass: String,
    @ColumnInfo(name = "effect") val effect: String?,
    @ColumnInfo(name = "statics") val statics: List<String>,
    @ColumnInfo(name = "constellation") val constellation: String,
)