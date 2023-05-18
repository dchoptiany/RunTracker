package com.example.runtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runtracker.converters.DateConverter
import com.example.runtracker.converters.PointsListConverter

@Database(entities = [Run::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, PointsListConverter::class)
abstract class RunDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao

    companion object {
        @Volatile
        private var INSTANCE: RunDatabase? = null

        fun getDatabase(context: Context): RunDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RunDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}