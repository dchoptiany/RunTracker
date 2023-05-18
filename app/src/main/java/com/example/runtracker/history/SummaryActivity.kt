package com.example.runtracker.history

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.runtracker.R
import com.example.runtracker.RunApplication
import com.example.runtracker.database.Run
import com.example.runtracker.database.RunModelFactory
import com.example.runtracker.database.RunViewModel
import kotlin.math.floor
import kotlin.math.round

class SummaryActivity : AppCompatActivity() {
    private val viewModel: RunViewModel by viewModels {
        RunModelFactory((application as RunApplication).repository)
    }

    private lateinit var textViewDistance: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewPace: TextView
    // TODO: add map with marked path
    // TODO: add gallery of photos from run

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        textViewDistance = findViewById(R.id.textViewDistance)
        textViewTime = findViewById(R.id.textViewTime)
        textViewPace = findViewById(R.id.textViewPace)

        if(intent != null) {
            val runID = intent.getIntExtra("runID", -1)
            val run: Run? = viewModel.runByID(runID).value
            if(run == null) {
                return
            }

            val distanceKilometers = run.distance
            val timeSeconds = run.duration
            val hours = timeSeconds / 3600 // full hours
            val minutes = (timeSeconds - (hours * 3600)) / 60 // full minutes
            val seconds = timeSeconds % 60 // seconds

            val timeMinutes: Float = timeSeconds / 60f // exact minutes
            val paceMinPerKm: Float = timeMinutes / distanceKilometers // pace in minutes per kilometer
            val paceMinutes = floor(paceMinPerKm) // pace full minutes
            val paceSeconds = round((paceMinPerKm - paceMinutes) * 60)  // pace seconds (decimal part converted from minutes to seconds)

            textViewDistance.text = "$distanceKilometers km"
            textViewTime.text = "$hours:$minutes:$seconds"
            textViewPace.text = "$paceMinutes:$paceSeconds min/km"
        }
    }
}