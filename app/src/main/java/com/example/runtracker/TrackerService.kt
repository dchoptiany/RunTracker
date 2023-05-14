package com.example.runtracker

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import org.osmdroid.util.GeoPoint
import java.sql.Date
import java.util.*

class TrackerService() : Service() {
    companion object {
        const val TRACKER_UPDATED = "trackerUpdated"
        const val DIST_EXTRA = "distExtra"
        const val LAT_EXTRA = "latitudeExtra"
        const val LON_EXTRA = "longitudeExtra"
    }

    private val timer = Timer()

    lateinit var currentLocation : GeoPoint
    var latitude = 0.0
    var longitude = 0.0

    lateinit var run : Run
    var duration : Int = 0
    var distance : Float = 0f
    var points : MutableList<GeoPoint> = mutableListOf()

    lateinit var startingTime : java.sql.Date
    lateinit var stoppedTime : java.sql.Date

    val handler : Handler = Handler(Looper.getMainLooper())

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("mytracker", "ON START COMMAND")
        latitude = intent!!.getDoubleExtra(LAT_EXTRA, 0.0)
        longitude = intent!!.getDoubleExtra(LON_EXTRA, 0.0)
        currentLocation = GeoPoint(latitude, longitude)

        distance = intent!!.getFloatExtra(DIST_EXTRA, 0f)

        startTracking()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.i("mytracker", "ON DESTROY")
        stopTracking()
        super.onDestroy()
    }

    fun startTracking() {
        Log.i("mytracker", "START TRACKING")
        // set up new run
        var starting = Calendar.getInstance().time
        startingTime = Date(starting.time)

        // start recording location changes
        //handler.postDelayed(periodicTrackUpdate, 0)
        timer.scheduleAtFixedRate(DistanceTask(distance), 0, 1000)
    }

    fun stopTracking() {
        Log.i("mytracker", "STOP TRACKING")

        var stopped = Calendar.getInstance().time
        stoppedTime  = Date(stopped.time)
        duration = (stoppedTime.time - startingTime.time).toInt()


        // create run object based on collected data
        //run = Run(0, startingTime, distance, duration)

        //TODO: save run object in database

        // stop recording location changes
        //handler.removeCallbacks(periodicTrackUpdate)
        timer.cancel()
    }

    private inner class DistanceTask(private var distance : Float) : TimerTask() {
        override fun run() {
            Log.i("mytracker", "RUNNABLE")

            // UPDATE LOCATION

            var runDataPair : Pair<Float, MutableList<GeoPoint>> = RunHelper.addWayPointToRun(points, distance, currentLocation)
            distance = runDataPair.first
            points = runDataPair.second

            Log.i("mytracker", "SEND DISTANCE: " + distance.toString())
            var intent = Intent(TRACKER_UPDATED)

            intent.putExtra(DIST_EXTRA, distance)
            sendBroadcast(intent)

            var newLocation = getLocation()
            currentLocation = GeoPoint(newLocation.latitude, newLocation.longitude)
        }

        fun getLocation() : Location {
            var newLocation : Location = Location(LocationManager.GPS_PROVIDER)
            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val gpsLocationListener : LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    newLocation = location
                }
            }

            if(hasGPS) {
                if(ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Handler(Looper.getMainLooper()).post {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000,
                            0f,
                            gpsLocationListener
                        )
                    }
                }
            }

            var lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation.let {
                newLocation = lastKnownLocation!!
            }

            Log.i("mytracker", "NEW LOCATION LAT:" + newLocation.latitude + " LON: " + newLocation.longitude )
            return newLocation
        }
    }
}