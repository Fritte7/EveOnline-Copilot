package com.fritte.eveonline.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fritte.eveonline.data.room.dao.ConstellationDao
import com.fritte.eveonline.data.room.dao.RegionDao
import com.fritte.eveonline.data.room.dao.SystemDao
import com.fritte.eveonline.data.room.entities.Constellation
import com.fritte.eveonline.data.room.entities.Region
import com.fritte.eveonline.data.room.entities.System
import com.fritte.eveonline.data.room.utils.Converters

@Database(
    entities = [
        System::class,
        Region::class,
        Constellation::class,
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
}