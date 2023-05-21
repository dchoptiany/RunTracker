package com.example.runtracker

import android.app.Application
import com.example.runtracker.database.RunDatabase
import com.example.runtracker.database.RunRepository

class RunApplication: Application() {
    private val database by lazy { RunDatabase.getDatabase(this) }
    val repository by lazy { RunRepository(database.runDao()) }
}