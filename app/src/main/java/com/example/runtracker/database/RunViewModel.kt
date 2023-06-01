package com.example.runtracker.database

import androidx.lifecycle.*
import java.time.LocalDateTime

class RunViewModel(private val repository: RunRepository): ViewModel() {
    var runs: LiveData<List<Run>> = repository.allRuns.asLiveData()
    var numberOfRuns: LiveData<Int> = repository.numberOfRuns.asLiveData()
    var totalDistance: LiveData<Float> = repository.totalDistance.asLiveData()
    var maxRunID: LiveData<Int> = repository.maxRunID.asLiveData()
    var totalDuration: LiveData<Int> = repository.totalDuration.asLiveData()
    var numberOfPins: LiveData<Int> = repository.numberOfPins.asLiveData()
    var longestDistance: LiveData<Float> = repository.longestDistance.asLiveData()
    var longestDuration: LiveData<Int> = repository.longestDuration.asLiveData()
    var maxBurnedCalories: LiveData<Float> = repository.maxBurnedCalories.asLiveData()
    var totalBurnedCalories: LiveData<Float> = repository.totalCaloriesBurned.asLiveData()


    fun runsByDate(dateTime: LocalDateTime): LiveData<List<Run>> {
        return repository.getByDate(dateTime).asLiveData()
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

    fun insertPin(pin: Pin) {
        repository.insertPin(pin)
    }

    fun getPins(runID: Int): LiveData<List<Pin>> {
        return repository.getPins(runID).asLiveData()
    }

    fun getAllPins(): LiveData<List<Pin>> {
        return repository.getAllPins().asLiveData()
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