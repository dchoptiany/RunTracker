package com.example.runtracker

import com.example.runtracker.converters.DateConverter
import org.junit.Assert.assertEquals
import org.junit.Test
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
}