package com.example.runtracker.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import java.sql.Date

class RunViewModel(private val repository: RunRepository): ViewModel() {
    var runs: LiveData<List<Run>> = repository.allRuns.asLiveData()
    var numberOfRuns: LiveData<Int> = repository.numberOfRuns.asLiveData()
    var totalDistance: LiveData<Float> = repository.totalDistance.asLiveData()

    fun runsByDate(date: Date): LiveData<List<Run>> {
        return repository.getByDate(date).asLiveData()
    }

    fun runByID(ID: Int): LiveData<Run> {
        return repository.getByID(ID).asLiveData()
    }

    fun insertRun(run: Run) {
        repository.insertRun(run)
    }

    fun insertRuns(vararg runs: Run) {
        repository.insertRuns(*runs)
    }

    fun deleteByID(runID: Int) {
        repository.deleteByID(runID)
    }

    fun deleteRun(run: Run) {
        repository.delete(run)
    }

    fun deleteAllRuns() {
        repository.deleteAll()
    }

    fun updateRun(run: Run) {
        repository.updateRun(run)
    }


    fun insertGeoPoint(geoPointsEntity: GeoPointsEntity){
        repository.insertGeoPoint(geoPointsEntity)
    }


    fun getPins(runID : Int): LiveData<List<GeoPointsEntity>>{
        return repository.getPins(runID).asLiveData()
    }


    fun getGeoPoints(runID : Int): LiveData<List<GeoPointsEntity>>{
        return repository.getGeoPoints(runID).asLiveData()
    }

    fun getAllGeoPoints(): LiveData<List<GeoPointsEntity>>{
       return  repository.getAllGeoPoints().asLiveData()
    }
}

class RunModelFactory(private val repository: RunRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunViewModel::class.java)) {
            return RunViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}