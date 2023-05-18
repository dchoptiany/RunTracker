package com.example.runtracker.converters

import androidx.room.TypeConverter
import java.sql.Date

class DateConverter {
    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return Date.valueOf(dateString)
    }

    @TypeConverter
    fun toString(date: Date?): String? {
        return date?.toString()
    }
}