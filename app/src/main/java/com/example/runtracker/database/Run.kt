package com.example.runtracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime

@Entity
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "dateTime") val dateTime: LocalDateTime, // date and time of start
    @ColumnInfo(name = "distance") val distance: Float, // recorded distance in kilometers
    @ColumnInfo(name = "duration") val duration: Int, // recorded duration in seconds
    @ColumnInfo(name = "points") val points: MutableList<GeoPoint>, // points in recorded path
    @ColumnInfo(name = "calories") val calories: Float // number of calories burned
)