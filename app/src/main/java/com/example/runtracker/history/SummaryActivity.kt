package com.example.runtracker.history

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.runtracker.R
import com.example.runtracker.RunApplication
import com.example.runtracker.database.RunModelFactory
import com.example.runtracker.database.RunViewModel
import com.example.runtracker.statistics.StringFormatter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.round

class SummaryActivity : AppCompatActivity() {
    private var runID: Int = -1

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
            runID = intent.getIntExtra("runID", -1)
            viewModel.runByID(runID).observe(this) {
                if(it == null) {
                    return@observe
                }

                val timeSeconds = it.duration
                val distanceKilometers = it.distance

                val timeMinutes: Float = timeSeconds / 60f // exact minutes
                val paceMinPerKm: Float =
                    timeMinutes / distanceKilometers // pace in minutes per kilometer

                textViewDistance.text = StringFormatter.getInstance().formatDistance(distanceKilometers)
                textViewTime.text = StringFormatter.getInstance().formatTime(timeSeconds)
                textViewPace.text = StringFormatter.getInstance().formatPace(paceMinPerKm)
            }
        }

        findViewById<Button>(R.id.buttonDeleteRun).setOnClickListener {
            buttonDeleteClicked(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun buttonDeleteClicked(view: View) {
        GlobalScope.launch {
            viewModel.deleteByID(runID)
            finish()
        }
    }
}