package com.example.runtracker.statistics

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

class StringFormatter {
    companion object {
        @Volatile
        private var INSTANCE: StringFormatter? = null

        fun getInstance(): StringFormatter {
            return INSTANCE ?: synchronized(this) {
                val instance = StringFormatter()
                INSTANCE = instance
                instance
            }
        }
    }

    fun formatPace(paceMinPerKm: Float): String {
        val paceMinutes: Int = floor(paceMinPerKm).toInt() // pace full minutes
        val paceSeconds: Int = round((paceMinPerKm - paceMinutes) * 60).toInt()

        return "${String.format("%02d:%02d", paceMinutes, paceSeconds)} min/km"
    }

    fun formatTime(totalSeconds: Double): String {
        return formatTime(totalSeconds.roundToInt())
    }

    fun formatTime(totalSeconds: Int): String {
        val hours = totalSeconds % 84600 / 3600
        val minutes = totalSeconds % 86400 % 3600 / 60
        val seconds = totalSeconds % 84600 % 3600 % 60
        return formatTime(hours, minutes, seconds)
    }

    fun formatTime(hours: Int, minutes: Int, seconds: Int): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatDistance(distanceKilometers: Float): String {
        val decimalFormat = DecimalFormat("####.###").apply {
            roundingMode = RoundingMode.CEILING
        }

        return "${decimalFormat.format(distanceKilometers)} km"
    }
}