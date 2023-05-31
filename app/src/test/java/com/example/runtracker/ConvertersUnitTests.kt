package com.example.runtracker

import com.example.runtracker.converters.LocalDateTimeConverter
import com.example.runtracker.converters.PointsListConverter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime

class ConvertersUnitTests {
    @Test
    fun localDateTimeConverterTest() {
        val dateTime = LocalDateTime.parse("2023-12-31T12:30:45")
        val converter = LocalDateTimeConverter()
        val converted = converter.toString(dateTime)
        val dateTimeFromString = converter.toLocalDateTime(converted)
        assertEquals(dateTime, dateTimeFromString)
    }

    @Test
    fun pointsListConverterTest() {
        val points = mutableListOf<GeoPoint>()
        points.add(GeoPoint(52.237049, 21.017532))
        points.add(GeoPoint(51.107883, 17.038538))
        points.add(GeoPoint(54.372158, 18.638306))
        points.add(GeoPoint(50.049683, 19.944544))
        val converter = PointsListConverter()
        val converted = converter.mutableListToString(points)
        val pointsFromString = converter.toMutableList(converted)
        assertEquals(points, pointsFromString)
    }
}