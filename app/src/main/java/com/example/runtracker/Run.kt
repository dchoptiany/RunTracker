package com.example.runtracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint
import java.sql.Date

@Entity
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "distance") val distance: Float, // recorded distance in kilometers
    @ColumnInfo(name = "duration") val duration: Int, // recorded duration in seconds
    @ColumnInfo(name = "points") val points: MutableList<GeoPoint> // points in recorded path
)