package com.example.runtracker.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.osmdroid.util.GeoPoint

class PointsListConverter {
    @TypeConverter
    fun mutableListToString(list: MutableList<GeoPoint>): String {
        val listOfSerializedPoints: ArrayList<String> = ArrayList()
        for(point in list) {
            listOfSerializedPoints.add(geoPointToString(point))
        }
        return Json.encodeToString(listOfSerializedPoints)
    }

    @TypeConverter
    fun toMutableList(value: String): MutableList<GeoPoint> {
        val listOfSerializedPoints: List<String> = Json.decodeFromString(value)
        val list: MutableList<GeoPoint> = ArrayList()
        for(serializedPoint in listOfSerializedPoints) {
            list.add(toGeoPoint(serializedPoint))
        }
        return list
    }

    @TypeConverter
    fun toGeoPoint(pointString: String): GeoPoint {
        return GeoPoint.fromDoubleString(pointString, ',')
    }

    @TypeConverter
    fun geoPointToString(point: GeoPoint): String {
        return point.toDoubleString()
    }
}