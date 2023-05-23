package com.example.runtracker.database

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface GeoPointsDao {
    @Query("SELECT * FROM GeoPointsEntity")
    fun getAll(): Flow<List<GeoPointsEntity>>

    @Insert
    fun insert(geoPoints: GeoPointsEntity)

    @Query("SELECT * FROM GeoPointsEntity WHERE isPinned == false")
    fun getRunGeoPoints(): Flow<List<GeoPointsEntity>>

    @Query("SELECT * FROM GeoPointsEntity WHERE isPinned == true")
    fun getPins(): Flow<List<GeoPointsEntity>>
}