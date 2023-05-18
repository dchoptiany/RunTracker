package com.example.runtracker.database

import androidx.annotation.WorkerThread
import com.example.runtracker.database.Run
import com.example.runtracker.database.RunDao
import kotlinx.coroutines.flow.Flow
import java.sql.Date

class RunRepository(private val runDao: RunDao) {
    val allRuns: Flow<List<Run>> = runDao.getAll()
    val numberOfRuns: Flow<Int> = runDao.getNumberOfRuns()
    val totalDistance: Flow<Float> = runDao.getTotalDistance()

    @WorkerThread
    fun getByDate(date: Date): Flow<List<Run>> {
        return runDao.getByDate(date)
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
}