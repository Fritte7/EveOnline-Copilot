package com.fritte.eveonline.data.room.utils

import androidx.room.TypeConverter

class Converters {

    // List<String> <-> String
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(",")

    // List<Int> <-> String
    @TypeConverter
    fun fromIntList(value: List<Int>): String = value.joinToString(",")

    @TypeConverter
    fun toIntList(value: String): List<Int> =
        if (value.isBlank()) emptyList() else value.split(",").mapNotNull { it.toIntOrNull() }
}
