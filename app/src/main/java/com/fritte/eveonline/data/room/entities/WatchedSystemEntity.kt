package com.fritte.eveonline.data.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "watched_system",
    foreignKeys = [
        ForeignKey(
            entity = SystemEntity::class,
            parentColumns = ["systemId"],
            childColumns = ["systemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WatchedSystemEntity(
    @PrimaryKey val systemId: Long,
    val createdAt: Long = System.currentTimeMillis()
)
