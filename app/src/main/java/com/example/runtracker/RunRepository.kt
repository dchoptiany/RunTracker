package com.example.runtracker

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RunRepository(private val runDao: RunDao) {
    val allRuns: Flow<List<Run>> = runDao.getAll()

    @WorkerThread
    suspend fun insertRun(run: Run) {
        runDao.insert(run)
    }
}