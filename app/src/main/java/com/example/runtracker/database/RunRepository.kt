package com.example.runtracker.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class RunRepository(private val runDao: RunDao) {
    val allRuns: Flow<List<Run>> = runDao.getAll()
    val numberOfRuns: Flow<Int> = runDao.getNumberOfRuns()
    val totalDistance: Flow<Float> = runDao.getTotalDistance()
    val maxRunID: Flow<Int> = runDao.getMaxRunID()
    val totalDuration: Flow<Int> = runDao.getTotalDuration()
    val numberOfPins: Flow<Int> = runDao.getNumberOfPins()
    val longestDistance: Flow<Float> = runDao.getLongestDistance()
    val longestDuration: Flow<Int> = runDao.getLongestDuration()
    val maxBurnedCalories: Flow<Float> = runDao.getMaxCalories()
    val totalCaloriesBurned: Flow<Float> = runDao.getTotalCalories()


    @WorkerThread
    fun getByDate(dateTime: LocalDateTime): Flow<List<Run>> {
        return runDao.getByDate(dateTime)
    }

    @WorkerThread
    fun getByID(ID: Int): Flow<Run> {
        return runDao.getByID(ID)
    }

    @WorkerThread
    fun insertRun(run: Run) {
        runDao.insert(run)
    }

    @WorkerThread
    fun insertRuns(vararg runs: Run) {
        runDao.insertAll(*runs)
    }

    @WorkerThread
    fun deleteByID(runID: Int) {
        runDao.deleteByID(runID)
        runDao.deletePinByID(runID)
    }

    @WorkerThread
    fun delete(run: Run) {
        runDao.delete(run)
    }

    @WorkerThread
    fun deleteAll() {
        runDao.deleteAll()
    }

    @WorkerThread
    fun updateRun(run: Run) {
        runDao.update(run)
    }


    @WorkerThread
    fun insertPin(pin: Pin) {
        runDao.insertPin(pin)
    }

    @WorkerThread
    fun getPins(runID: Int): Flow<List<Pin>> {
        return runDao.getPins(runID)
    }

    @WorkerThread
    fun getAllPins(): Flow<List<Pin>> {
        return runDao.getAllPins()
    }
}