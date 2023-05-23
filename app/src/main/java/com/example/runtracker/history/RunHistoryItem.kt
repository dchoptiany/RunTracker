package com.example.runtracker.history

import java.math.RoundingMode
import java.sql.Date
import java.text.DecimalFormat
import java.util.*

class RunHistoryItem(private var runID: Int, private var date: Date, var distance: Float, var duration: Int) {
    fun getName(): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
        val hourOfDay: Int = calendar.get(Calendar.HOUR_OF_DAY)
        return "${getDayOfWeek(dayOfWeek)} ${getTimeOfDay(hourOfDay)} Run"
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

    private fun getDayOfWeek(day: Int): String {
        when(day) {
            Calendar.SUNDAY -> return "Sunday"
            Calendar.MONDAY -> return "Monday"
            Calendar.TUESDAY -> return "Tuesday"
            Calendar.WEDNESDAY -> return "Wednesday"
            Calendar.THURSDAY -> return "Thursday"
            Calendar.FRIDAY -> return "Friday"
        }
        return "Saturday"
    }

    fun getRunID(): Int {
        return runID
    }
}