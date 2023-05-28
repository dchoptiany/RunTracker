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

    @Query("SELECT MAX(id) FROM run")
    fun getMaxRunID(): Flow<Int>

    @Insert
    fun insertPin(pin: Pin)

    @Query("SELECT MAX(calories) FROM run")
    fun getMaxCalories() : Flow<Float>

    @Query("SELECT SUM(calories) FROM run")
    fun getTotalCalories() : Flow<Float>

    @Query("SELECT * FROM Pin WHERE runId ==:runID")
    fun getPins(runID: Int): Flow<List<Pin>>

    @Query("SELECT * FROM Pin")
    fun getAllPins(): Flow<List<Pin>>

    @Query("SELECT COUNT(*) FROM Pin")
    fun getNumberOfPins(): Flow<Int>
}