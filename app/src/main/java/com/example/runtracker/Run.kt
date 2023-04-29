package com.example.runtracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Run(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "distance") val distance: Float, // recorded distance in kilometers
    @ColumnInfo(name = "duration") val duration: Int, // recorded duration in seconds
    // TODO: add recorded path
)