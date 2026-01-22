package com.fritte.eveonline.data.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "visited_system",
    foreignKeys = [
        ForeignKey(
            entity = SystemEntity::class,
            parentColumns = ["systemId"],
            childColumns = ["systemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["systemId"]),
        Index(value = ["visitedAt"]),
        Index(value = ["visitedAt", "systemId"])
    ]
)
data class VisitedSystemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val systemId: Long,
    val visitedAt: Long
)