package com.example.runtracker

import androidx.room.*
import java.sql.Date

@Dao
interface RunDao {
    @Query("SELECT * FROM run")
    fun getAll(): List<Run>

    @Query("SELECT * FROM run WHERE date == :date")
    fun getByDate(date: Date): List<Run>
    
    @Query("SELECT COUNT(*) FROM run")
    fun getNumberOfRuns(): Int

    @Query("SELECT SUM(distance) FROM run")
    fun getTotalDistance(): Float

    @Insert
    fun insertAll(vararg runs: Run)

    @Delete
    fun delete(run: Run)

    @Update
    fun update(run: Run)
}