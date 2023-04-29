package com.example.runtracker

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class RunViewModel(private val repository: RunRepository): ViewModel() {
    var runs: LiveData<List<Run>> = repository.allRuns.asLiveData()

    fun addRun(run: Run) = viewModelScope.launch {
        repository.insertRun(run)
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