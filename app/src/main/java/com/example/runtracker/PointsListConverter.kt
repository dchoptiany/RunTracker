package com.example.runtracker

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.osmdroid.util.GeoPoint

class PointsListConverter {
    @TypeConverter
    fun toString(list: MutableList<GeoPoint>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toMutableList(value: String): MutableList<GeoPoint> {
        return Json.decodeFromString(value)
    }
}