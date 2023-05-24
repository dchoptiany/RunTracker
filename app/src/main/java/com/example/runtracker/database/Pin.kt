package com.example.runtracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint

@Entity
data class Pin(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "geoPoint") val geoPoint : GeoPoint,
    @ColumnInfo(name = "imagePath") val image_path: String,
    @ColumnInfo(name = "runId") val runId: Int
)
