package com.example.runtracker.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString)
    }

    @TypeConverter
    fun toString(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }
}