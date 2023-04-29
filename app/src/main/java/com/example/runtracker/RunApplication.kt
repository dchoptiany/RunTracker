package com.example.runtracker

import android.app.Application

class RunApplication: Application() {
    private val database by lazy { RunDatabase.getDatabase(this) }
    val repository by lazy { RunRepository(database.runDao()) }
}