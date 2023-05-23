package com.example.runtracker.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface RunDao {
    @Query("SELECT * FROM run")
    fun getAll(): Flow<List<Run>>

    @Query("SELECT * FROM run WHERE date == :date")
    fun getByDate(date: Date): Flow<List<Run>>

    @Query("SELECT * FROM run WHERE id == :runID")
    fun getByID(runID: Int): Flow<Run>

    @Query("SELECT COUNT(*) FROM run")
    fun getNumberOfRuns(): Flow<Int>

    @Query("SELECT SUM(distance) FROM run")
    fun getTotalDistance(): Flow<Float>

    @Query("SELECT SUM(duration) FROM run")
    fun getTotalDuration(): Flow<Int>

    @Query("SELECT MAX(distance) FROM run")
    fun getLongestDistance(): Flow<Float>

    @Query("SELECT MAX(duration) FROM run")
    fun getLongestDuration(): Flow<Int>

    @Insert
    fun insert(run: Run)

    @Insert
    fun insertAll(vararg runs: Run)

    @Delete
    fun delete(run: Run)

    @Query("DELETE FROM run WHERE id = :runID")
    fun deleteByID(runID: Int)

    @Query("DELETE FROM run")
    fun deleteAll()

    @Update
    fun update(run: Run)

    @Insert
    fun insertGeoPoints(geoPoints: GeoPointsEntity)


    @Query("SELECT * FROM GeoPointsEntity WHERE runId ==:runID")
    fun getPins(runID: Int): Flow<List<GeoPointsEntity>>

    @Query("SELECT * FROM GeoPointsEntity")
    fun getAllPins(): Flow<List<GeoPointsEntity>>
}