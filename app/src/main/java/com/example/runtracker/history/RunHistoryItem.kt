package com.example.runtracker.history

import java.sql.Date
import java.util.*

class RunHistoryItem(private var runID: Int, private var date: Date, private var distance: Float, private var duration: Int) {
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

    fun getDistance(): String {
        return "$distance km"
    }

    fun getDuration(): String {
        val hours = duration / 3600 // full hours
        val minutes = (duration - (hours * 3600)) / 60 // full minutes
        val seconds = duration % 60 // seconds

        return "${hours.toString().padStart(2, '0')}:" +
                "${minutes.toString().padStart(2, '0')}:" +
                seconds.toString().padStart(2, '0')
    }

    fun getRunID(): Int {
        return runID
    }
}