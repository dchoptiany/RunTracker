package com.example.runtracker

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Run::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
}