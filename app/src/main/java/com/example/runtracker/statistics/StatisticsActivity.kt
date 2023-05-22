package com.example.runtracker.statistics

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        viewModel.numberOfRuns.observe(this) {
            findViewById<TextView>(R.id.textViewTotalRuns).text = it.toString()
        }

        viewModel.totalDistance.observe(this) {
            findViewById<TextView>(R.id.textViewTotalDistance).text =
                StringFormatter.getInstance().formatDistance(it)
        }

        viewModel.totalDuration.observe(this) {
            findViewById<TextView>(R.id.textViewTotalDuration).text =
                StringFormatter.getInstance().formatTime(it)
        }

        /*viewModel.totalPhotos.observe(this) {
            findViewById<TextView>(R.id.textViewTotalPhotos).text = it.toString()
        }*/

        viewModel.longestDistance.observe(this) {
            findViewById<TextView>(R.id.textViewLongestDistance).text =
                StringFormatter.getInstance().formatDistance(it)
        }

        viewModel.longestDuration.observe(this) {
            findViewById<TextView>(R.id.textViewLongestDuration).text =
                StringFormatter.getInstance().formatTime(it)
        }

        /*viewModel.mostPhotosInRun.observe(this) {
            findViewById<TextView>(R.id.textViewMostPhotos).text = it.toString()
        }*/
    }
}