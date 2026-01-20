package com.fritte.eveonline.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fritte.eveonline.data.room.dao.ConstellationDao
import com.fritte.eveonline.data.room.dao.RegionDao
import com.fritte.eveonline.data.room.dao.SystemDao
import com.fritte.eveonline.data.room.dao.VisitedSystemDao
import com.fritte.eveonline.data.room.entities.ConstellationEntity
import com.fritte.eveonline.data.room.entities.RegionEntity
import com.fritte.eveonline.data.room.entities.SystemEntity
import com.fritte.eveonline.data.room.utils.Converters

@Database(
    entities = [
        SystemEntity::class,
        RegionEntity::class,
        ConstellationEntity::class,
    ],
    version = 1
)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun systemDao(): SystemDao
    abstract fun regionDao(): RegionDao
    abstract fun constellationDao(): ConstellationDao
    abstract fun visitedSystemDao(): VisitedSystemDao
}