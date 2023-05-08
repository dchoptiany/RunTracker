package com.example.runtracker

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.sql.Date

class RunViewModel(private val repository: RunRepository): ViewModel() {
    var runs: LiveData<List<Run>> = repository.allRuns.asLiveData()
    var numberOfRuns: LiveData<Int> = repository.numberOfRuns.asLiveData()
    var totalDistance: LiveData<Float> = repository.totalDistance.asLiveData()

    fun runsByDate(date: Date): LiveData<List<Run>> {
        var runs: LiveData<List<Run>>? = null
        viewModelScope.launch {
            runs = repository.getByDate(date).asLiveData()
        }
        return runs as LiveData<List<Run>>
    }

    fun runByID(ID: Int): LiveData<Run> {
        var run: LiveData<Run>? = null
        viewModelScope.launch {
            run = repository.getByID(ID).asLiveData()
        }
        return run as LiveData<Run>
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        repository.insertRun(run)
    }

    fun insertRuns(vararg runs: Run) = viewModelScope.launch {
        repository.insertRuns(*runs)
    }

    fun deleteRun(run: Run) = viewModelScope.launch {
        repository.delete(run)
    }

    fun deleteAllRuns() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun updateRun(run: Run) = viewModelScope.launch {
        repository.updateRun(run)
    }
}

class RunModelFactory(private val repository: RunRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RunViewModel::class.java)) {
            return RunViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}