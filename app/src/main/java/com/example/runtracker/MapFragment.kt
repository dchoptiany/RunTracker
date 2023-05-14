package com.example.runtracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.github.clans.fab.FloatingActionButton
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt


class MapFragment : Fragment() {
    lateinit var addPhotoButton : FloatingActionButton
    lateinit var startButton : ImageButton
    lateinit var pauseButton : ImageButton
    lateinit var stopButton : ImageButton
    lateinit var mapView : MapView
    lateinit var timeTextView: TextView
    lateinit var distanceTextView: TextView
    lateinit var paceTextView: TextView

    var timerStarted = false
    lateinit var serviceIntent : Intent
    var time = 0.0

    lateinit var trackerIntent : Intent
    var distance : Float = 0f

    lateinit var mapController : IMapController
    lateinit var myGpsMyLocationProvider : GpsMyLocationProvider
    lateinit var myLocationOverlay : MyLocationNewOverlay
    lateinit var currentLocation : GeoPoint

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_map, container, false)

        // text view setup
        timeTextView = view!!.findViewById(R.id.timeTextView) as TextView
        distanceTextView = view!!.findViewById(R.id.distanceTextView) as TextView
        paceTextView = view!!.findViewById(R.id.paceTextView) as TextView

        // button setup
        addPhotoButton = view!!.findViewById(R.id.addPhotoFAB) as FloatingActionButton
        addPhotoButton.setOnClickListener {
            addPhoto()
        }

        startButton = view!!.findViewById(R.id.startButton) as ImageButton
        startButton.setOnClickListener {
            startActivity()
        }

        pauseButton = view!!.findViewById(R.id.pauseButton) as ImageButton
        pauseButton.setOnClickListener {
            pauseActivity()
        }

        stopButton = view!!.findViewById(R.id.stopButton) as ImageButton
        stopButton.setOnClickListener {
            stopActivity()
        }

        // map setup
        mapView = view!!.findViewById(R.id.mapView) as MapView

        var context = requireActivity().applicationContext
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        mapView.setUseDataConnection(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        mapController = mapView.controller
        mapController.zoomTo(14, 1)

        var defaultLocation : GeoPoint = GeoPoint(51.1077,17.0625) // Wrocław University of Science and Technlogy
        mapController.animateTo(defaultLocation)

        // set current location
        if(isLocationPermissionGranted()) {
            Log.i("mymap", "Localization permission granted")
            myGpsMyLocationProvider = GpsMyLocationProvider(activity)
            myLocationOverlay = MyLocationNewOverlay(myGpsMyLocationProvider, mapView)
            myLocationOverlay.enableMyLocation()
            myLocationOverlay.enableFollowLocation()
            myLocationOverlay.isDrawAccuracyEnabled = true

            val icon = BitmapFactory.decodeResource(
                resources,
                org.osmdroid.library.R.drawable.ic_menu_compass
            )
            myLocationOverlay.setPersonIcon(icon)
            mapView.overlays.add(myLocationOverlay)

            myLocationOverlay.runOnFirstFix(Runnable {
                val myLocation: GeoPoint = myLocationOverlay.getMyLocation()
                if (myLocation != null) {
                    requireActivity().runOnUiThread {
                        mapView.getController().animateTo(myLocation)
                    }
                }
            })
        }

        // timer service setup
        serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireActivity().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        // tracker service setup
        trackerIntent = Intent(requireContext(), TrackerService::class.java)
        requireActivity().registerReceiver(updateDistance, IntentFilter(TrackerService.TRACKER_UPDATED))

        return view
    }

    val updateTime : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent : Intent) {
            Log.i("mytracker", "UPDATE TIME")
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            timeTextView.text = getTimeStringFromDouble(time)
        }
    }

    val updateDistance : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val df = DecimalFormat("#.###")
            df.roundingMode = RoundingMode.CEILING
            distance = intent.getFloatExtra(TrackerService.DIST_EXTRA, 0f) // distance in meters
            Log.i("mytracker", "UPDATE DISTANCE: " + distance.toString())
            distanceTextView.text = df.format(distance / 1000) + " km"
        }
    }

    fun getTimeStringFromDouble(time : Double) : String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 84600 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 84600 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        requireActivity().startService(serviceIntent)
    }

    private fun pauseTimer() {
        requireActivity().stopService(serviceIntent)
    }

    private fun resetTimer() {
        pauseTimer()
        time = 0.0
        timeTextView.text = getTimeStringFromDouble(time)
    }

    fun addPhoto() {
        Toast.makeText(requireContext(), "Add Photo!", Toast.LENGTH_SHORT).show()
    }

    fun startActivity() {
        Toast.makeText(requireContext(), "Start Running!", Toast.LENGTH_SHORT).show()

        startTimer()

        currentLocation = LocationHelper.getLastKnownLocation(myLocationOverlay)



        trackerIntent.putExtra(TrackerService.LAT_EXTRA, currentLocation.latitude)
        trackerIntent.putExtra(TrackerService.LON_EXTRA, currentLocation.longitude)
        trackerIntent.putExtra(TrackerService.DIST_EXTRA, distance)
        Log.i("mytracker", "FRAGMENT STARTS TRACKER SERVICE")
        requireActivity().startService(trackerIntent)
        Log.i("mytracker", "AFTER FRAGMENT STARTS TRACKER SERVICE")
    }

    fun pauseActivity() {
        Toast.makeText(requireContext(), "Running paused!", Toast.LENGTH_SHORT).show()

        pauseTimer()
        requireActivity().stopService(trackerIntent)
    }

    fun stopActivity() {
        Toast.makeText(requireContext(), "Running stopped!", Toast.LENGTH_SHORT).show()

        resetTimer()
        Log.i("mytracker", "FRAGMENT STOPS TRACKER SERVICE")
        requireActivity().stopService(trackerIntent)

        // reset map overlay
    }

    fun isLocationPermissionGranted() : Boolean {
        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return false
        } else {
            return true
        }
    }
}