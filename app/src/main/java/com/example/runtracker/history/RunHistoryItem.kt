package com.example.runtracker.history

import java.time.DayOfWeek
import java.time.LocalDateTime

class RunHistoryItem(private var runID: Int, private var dateTime: LocalDateTime, var distance: Float, var duration: Int) {
    fun getName(): String {
        return "${getDayOfWeek(dateTime.dayOfWeek)} ${getTimeOfDay(dateTime.hour)} Run"
    }

    private fun getTimeOfDay(hour: Int): String {
        return if(hour < 5 || hour > 21) {
            "Night"
        } else if(hour < 12) {
            "Morning"
        } else if(hour < 19) {
            "Afternoon"
        } else {
            "Evening"
        }
    }

    private fun getDayOfWeek(day: DayOfWeek): String {
        return when(day) {
            DayOfWeek.SUNDAY -> "Sunday"
            DayOfWeek.MONDAY -> "Monday"
            DayOfWeek.TUESDAY -> "Tuesday"
            DayOfWeek.WEDNESDAY -> "Wednesday"
            DayOfWeek.THURSDAY -> "Thursday"
            DayOfWeek.FRIDAY -> "Friday"
            else -> "Saturday"
        }
    }

    fun getRunID(): Int {
        return runID
    }
}