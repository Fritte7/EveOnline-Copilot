package com.fritte.eveonline.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "visited_system",
)
data class VisitedSystemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val systemId: Long,
    val visitedAt: String
)