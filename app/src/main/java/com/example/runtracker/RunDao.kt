package com.example.runtracker

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface RunDao {
    @Query("SELECT * FROM run")
    fun getAll(): Flow<List<Run>>

    @Query("SELECT * FROM run WHERE date == :date")
    fun getByDate(date: Date): Flow<List<Run>>
    
    @Query("SELECT COUNT(*) FROM run")
    fun getNumberOfRuns(): Int

    @Query("SELECT SUM(distance) FROM run")
    fun getTotalDistance(): Float

    @Insert
    fun insert(run: Run)

    @Insert
    fun insertAll(vararg runs: Run)

    @Delete
    fun delete(run: Run)

    @Query("DELETE FROM run")
    fun deleteAll()

    @Update
    fun update(run: Run)
}