package com.example.runtracker.runRecording

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
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
import com.example.runtracker.BuildConfig
import com.example.runtracker.R
import com.example.runtracker.gallery.CameraActivity
import com.example.runtracker.gallery.ImageDetailsActivity
import com.github.clans.fab.FloatingActionButton
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt


class MapFragment : Fragment() {
    companion object {
        const val ACTIVITY_STARTED = "activityStarted"
        const val ACTIVITY_PAUSED = "activityPaused"
        const val ACTIVITY_STOPPED = "activityStopped"
    }

    var activityStatus : String = ""

    lateinit var addPhotoButton : FloatingActionButton
    lateinit var startButton : ImageButton
    lateinit var stopButton : ImageButton
    lateinit var mapView : MapView
    lateinit var timeTextView: TextView
    lateinit var distanceTextView: TextView
    lateinit var paceTextView: TextView

    lateinit var serviceIntent : Intent
    var time = 0.0

    lateinit var trackerIntent : Intent
    var distance : Float = 0f

    var pace : Double = 0.0

    lateinit var mapController : IMapController
    lateinit var myGpsMyLocationProvider : GpsMyLocationProvider
    lateinit var myLocationOverlay : MyLocationNewOverlay
    lateinit var currentLocation : GeoPoint
    lateinit var roadManager : OSRMRoadManager
    lateinit var track : Road
    var points : MutableList<GeoPoint> = mutableListOf()

    val pins: ArrayList<OverlayItem> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("mytracker", "CREATE VIEW")

        // setup policy for threads
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_map, container, false)

        // text view setup
        timeTextView = view!!.findViewById(R.id.timeTextView) as TextView
        distanceTextView = view!!.findViewById(R.id.distanceTextView) as TextView
        paceTextView = view!!.findViewById(R.id.paceTextView) as TextView


        // button setup
        addPhotoButton = view!!.findViewById(R.id.addPhotoFAB) as FloatingActionButton


        startButton = view!!.findViewById(R.id.startButton) as ImageButton
        startButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#68A620"))

        startButton.setOnClickListener {
            if(activityStatus != ACTIVITY_STARTED) {
                startActivity()
                startButton.setImageResource(R.drawable.pause_button_image)
                startButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFE338"))
            } else { //ACTIVITY_STARTED
                pauseActivity()
                startButton.setImageResource(R.drawable.play_button_image)
                startButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#68A620"))
            }
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

        roadManager = OSRMRoadManager(context, BuildConfig.APPLICATION_ID)

        mapView.setUseDataConnection(true)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        mapController = mapView.controller
        mapController.zoomTo(18, 1)

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
                requireActivity().runOnUiThread {
                    mapView.getController().animateTo(myLocation)
                }
            })
        }

        addPhotoButton.setOnClickListener {
            createPin()
            addPhoto()
        }

        // timer service setup
        serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireActivity().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        // tracker service setup
        trackerIntent = Intent(requireContext(), TrackerService::class.java)
        requireActivity().registerReceiver(updateTrack, IntentFilter(TrackerService.TRACKER_UPDATED))

        return view
    }

    private fun createPin() {
        val currentPinLocation = LocationHelper.getLastKnownLocation(myLocationOverlay)
        val point = GeoPoint(currentPinLocation.latitude, currentPinLocation.longitude)
        val overlayItem = OverlayItem("Pin", "", point)
        pins.add(overlayItem)

        val overlay = ItemizedIconOverlay<OverlayItem>(
            pins,
            object : OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    val intent = Intent(requireContext(), ImageDetailsActivity::class.java)
                    intent.putExtra("latitude",currentPinLocation.latitude)
                    intent.putExtra("longitude",currentPinLocation.longitude)
                    startActivity(intent)
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    return false
                }
            },
            context
        )
        mapView.overlays.add(overlay);
    }

    val updateTime : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent : Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            timeTextView.text = getTimeStringFromDouble(time)
        }
    }

    val updateTrack : BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onReceive(context: Context, intent: Intent) {
            // read current location
            var latitude = intent.getDoubleExtra(TrackerService.LAT_EXTRA, 0.0)
            var longitude = intent.getDoubleExtra(TrackerService.LON_EXTRA, 0.0)
            var location = GeoPoint(latitude, longitude)
            points.add(location)

            // update distance
            if(activityStatus == ACTIVITY_STARTED) {
                distance = intent.getFloatExtra(TrackerService.DIST_EXTRA, 0f) // distance in meters
                distance = intent.getFloatExtra(TrackerService.DIST_EXTRA, 0f) // distance in meters
            }

            if(distance != 0f) {
                distanceTextView.text = getDistanceString(distance)
            }

            // update pace
            pace = time / distance.toDouble()
            paceTextView.text = getPaceString(pace)

            // draw track
            var pointsArrayList = ArrayList<GeoPoint>(points)
            track = roadManager.getRoad(pointsArrayList) // WIFI REQUIRED!

            var trackOverlay : Polyline = RoadManager.buildRoadOverlay(track)

            mapView.overlays.add(trackOverlay)
            mapView.invalidate()
        }
    }

    fun startActivity() {
        Toast.makeText(requireContext(), "Running started!", Toast.LENGTH_SHORT).show()

        // change status
        activityStatus = ACTIVITY_STARTED

        // get current location
        currentLocation = LocationHelper.getLastKnownLocation(myLocationOverlay)
        points.add(currentLocation)

        startTimer()
        startTracker()
    }

    fun startTracker() {
        trackerIntent.putExtra(TrackerService.LAT_EXTRA, currentLocation.latitude)
        trackerIntent.putExtra(TrackerService.LON_EXTRA, currentLocation.longitude)
        trackerIntent.putExtra(TrackerService.DIST_EXTRA, distance)

        requireActivity().startService(trackerIntent)
    }
    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)

        requireActivity().startService(serviceIntent)
    }

    fun pauseActivity() {
        Toast.makeText(requireContext(), "Running paused!", Toast.LENGTH_SHORT).show()

        // change status
        activityStatus = ACTIVITY_PAUSED

        pauseTimer()
        pauseTracker()
    }

    fun pauseTracker() {
        requireActivity().stopService(trackerIntent)

        // clear pace
        paceTextView.text = "00:00 min/km"
    }

    private fun pauseTimer() {
        requireActivity().stopService(serviceIntent)
    }

    fun stopActivity() {
        Toast.makeText(requireContext(), "Running stopped!", Toast.LENGTH_SHORT).show()

        // change status
        activityStatus = ACTIVITY_STOPPED

        resetTimer()
        stopTracker()
    }

    fun stopTracker() {
        requireActivity().stopService(trackerIntent)

        // clear live data variables
        distance = 0f
        distanceTextView.text = "0.000 km"
        paceTextView.text = "00:00 min/km"
    }
    private fun resetTimer() {
        pauseTimer()

        // clear time
        time = 0.0
        timeTextView.text = getTimeStringFromDouble(time)
    }

    fun addPhoto() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        val currentPinLocation = LocationHelper.getLastKnownLocation(myLocationOverlay)
        intent.putExtra("latitude",currentPinLocation.latitude)
        intent.putExtra("longitude",currentPinLocation.longitude)
        startActivity(intent)
    }

    fun isLocationPermissionGranted() : Boolean {
        if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return false
        } else {
            return true
        }
    }

    fun getTimeStringFromDouble(time : Double) : String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 84600 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 84600 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    fun getDistanceString(distance : Float) : String {
        val df = DecimalFormat("####.###")
        df.roundingMode = RoundingMode.CEILING

        return df.format(distance / 1000) + " km"
    }

    fun getPaceString(pace : Double) : String {
        var minutes : Int = pace.toInt() % 86400 % 3600 / 60
        var seconds : Int = pace.toInt() % 86400 % 3600 % 60

        return String.format("%02d:%02d", minutes, seconds) + " min/km"
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}