package com.example.runtracker.history

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.runtracker.BuildConfig
import com.example.runtracker.R
import com.example.runtracker.RunApplication
import com.example.runtracker.database.RunModelFactory
import com.example.runtracker.database.RunViewModel
import com.example.runtracker.statistics.StringFormatter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import java.io.File

class SummaryActivity : AppCompatActivity() {
    private var runID: Int = -1

    private val viewModel: RunViewModel by viewModels {
        RunModelFactory((application as RunApplication).repository)
    }

    private lateinit var textViewDistance: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewPace: TextView
    private lateinit var textCalories: TextView
    // TODO: add 'photo markers' to map
    private lateinit var mapView: MapView
    // TODO: add gallery of photos from run

    private lateinit var mapController: IMapController

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        textViewDistance = findViewById(R.id.textViewDistance)
        textViewTime = findViewById(R.id.textViewTime)
        textViewPace = findViewById(R.id.textViewPace)
        mapView = findViewById(R.id.mapView)
        textCalories = findViewById(R.id.textCalories)

        // map setup
        val context = applicationContext
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        mapView.setUseDataConnection(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        mapController = mapView.controller
        mapController.zoomTo(18, 1)

        // read run data
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

                val points = it.points
                val calories = String.format("%.2f",it.calories)

                textViewDistance.text = StringFormatter.getInstance().formatDistance(distanceKilometers)
                textViewTime.text = StringFormatter.getInstance().formatTime(timeSeconds)
                textViewPace.text = StringFormatter.getInstance().formatPace(paceMinPerKm)
                textCalories.text = "Calories burnt: $calories kcal"

                // draw track
                val pointsArrayList = ArrayList<GeoPoint>(points)
                val polylineTrack = Polyline()
                polylineTrack.setPoints(pointsArrayList)
                mapView.overlays.add(polylineTrack)
                mapView.invalidate()

                // set map's starting location
                val defaultLocation = points.get(0)
                mapController.animateTo(defaultLocation)
            }
        }

        findViewById<Button>(R.id.buttonDeleteRun).setOnClickListener {
            buttonDeleteClicked(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun buttonDeleteClicked(view: View) {
        GlobalScope.launch {
            deletePhotos(runID)
            viewModel.deleteByID(runID)
            finish()
        }
    }

    private fun deletePhotos(runID: Int) {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val subDirectory = File(directory, "$runID")

        subDirectory.deleteRecursively()
    }
}