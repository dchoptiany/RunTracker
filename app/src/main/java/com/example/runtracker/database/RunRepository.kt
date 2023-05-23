package com.example.runtracker.database

import androidx.annotation.WorkerThread
import androidx.room.Query
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
    fun deleteByID(runID: Int) {
        runDao.deleteByID(runID)
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
    fun insertGeoPoint(geoPointsEntity: GeoPointsEntity){
        runDao.insertGeoPoints(geoPointsEntity)
    }

    @WorkerThread
    fun getPins(runID : Int) : Flow<List<GeoPointsEntity>>{
        return runDao.getPins(runID)
    }

    @WorkerThread
    fun getAllGeoPoints() : Flow<List<GeoPointsEntity>>{
        return runDao.getAllPins()
    }
}