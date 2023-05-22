package com.example.runtracker

import com.example.runtracker.converters.DateConverter
import com.example.runtracker.converters.PointsListConverter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.osmdroid.util.GeoPoint
import java.sql.Date

class ConvertersUnitTests {
    @Test
    fun dateConverterTest() {
        val date: Date = Date.valueOf("2022-12-31")
        val converter = DateConverter()
        val converted = converter.toString(date)
        val dateFromString = converter.toDate(converted)
        assertEquals(date, dateFromString)
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