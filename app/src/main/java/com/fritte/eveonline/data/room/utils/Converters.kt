package com.fritte.eveonline.data.room.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun fromLongList(v: List<Long>): String = v.joinToString(",")
    @TypeConverter fun toLongList(v: String): List<Long> = if (v.isBlank()) emptyList() else v.split(",").map { it.toLong() }
    @TypeConverter fun fromStringList(v: List<String>): String = v.joinToString(",")
    @TypeConverter fun toStringList(v: String): List<String> = if (v.isBlank()) emptyList() else v.split(",")
}

