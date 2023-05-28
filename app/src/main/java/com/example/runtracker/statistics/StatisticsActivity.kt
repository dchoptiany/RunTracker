package com.example.runtracker.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.runtracker.R
import com.example.runtracker.RunApplication
import com.example.runtracker.database.RunModelFactory
import com.example.runtracker.database.RunViewModel

class StatisticsActivity : AppCompatActivity() {
    private val viewModel: RunViewModel by viewModels {
        RunModelFactory((application as RunApplication).repository)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        viewModel.numberOfRuns.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewTotalRuns).text =
                    "Total runs:\n$it"
            }
        }

        viewModel.totalDistance.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewTotalDistance).text =
                    "Total distance:\n${StringFormatter.getInstance().formatDistance(it)}"
            }
        }

        viewModel.totalDuration.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewTotalDuration).text =
                    "Total duration:\n${StringFormatter.getInstance().formatTime(it)}"
            }
        }

        viewModel.numberOfPins.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewTotalPhotos).text =
                    "Total photos:\n$it"
            }
        }

        viewModel.longestDistance.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewLongestDistance).text =
                    "Longest distance:\n${StringFormatter.getInstance().formatDistance(it)}"
            }
        }

        viewModel.longestDuration.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewLongestDuration).text =
                    "Longest duration:\n${StringFormatter.getInstance().formatTime(it)}"
            }
        }

        viewModel.totalBurnedCalories.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewTotalCalories).text =
                    "Total burned calories:\n$it kcal"
            }
        }

        viewModel.maxBurnedCalories.observe(this) {
            if (it != null) {
                findViewById<TextView>(R.id.textViewMaxCalories).text =
                    "Most burned calories:\n$it kcal"
            }
        }
    }
}