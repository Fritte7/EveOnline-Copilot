package com.fritte.eveonline.data.room.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "visited_system",
    indices = [Index("systemName")]
)
data class VisitedSystemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val systemName: String,
    val visitedAt: String
)