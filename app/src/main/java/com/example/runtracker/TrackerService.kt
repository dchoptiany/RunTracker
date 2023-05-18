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

    lateinit var startingTime : Date
    lateinit var stoppedTime : Date

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        latitude = intent!!.getDoubleExtra(LAT_EXTRA, 0.0)
        longitude = intent!!.getDoubleExtra(LON_EXTRA, 0.0)
        currentLocation = GeoPoint(latitude, longitude)

        distance = intent!!.getFloatExtra(DIST_EXTRA, 0f)

        startTracking()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopTracking()
        super.onDestroy()
    }

    fun startTracking() {
        // get "run start" time
        var starting = Calendar.getInstance().time
        startingTime = Date(starting.time)

        // start recording location changes
        timer.scheduleAtFixedRate(DistanceTask(distance), 0, 1000)
    }

    fun stopTracking() {
        // get "run stop" time
        var stopped = Calendar.getInstance().time
        stoppedTime  = Date(stopped.time)

        // calc "run duration"
        duration = (stoppedTime.time - startingTime.time).toInt()

        // create run object based on collected data
        run = Run(0, startingTime, distance, duration, points)

        //TODO: save run object in database

        // stop recording location changes
        timer.cancel()
    }

    private inner class DistanceTask(private var distance : Float) : TimerTask() {
        override fun run() {
            // add way point
            var runDataPair : Pair<Float, MutableList<GeoPoint>> = RunHelper.addWayPointToRun(points, distance, currentLocation)
            distance = runDataPair.first
            points = runDataPair.second

            // get curr location
            var newLocation = getLocation()
            currentLocation = GeoPoint(newLocation.latitude, newLocation.longitude)

            // send data to MapFragment
            var intent = Intent(TRACKER_UPDATED)
            intent.putExtra(LAT_EXTRA, newLocation.latitude)
            intent.putExtra(LON_EXTRA, newLocation.longitude)
            intent.putExtra(DIST_EXTRA, distance)
            sendBroadcast(intent)
        }

        fun getLocation() : Location {
            var newLocation : Location = Location(LocationManager.NETWORK_PROVIDER)
            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            var networkLocationListener : LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    newLocation = location
                }
            }

            if(hasNetwork) {
                if(ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Handler(Looper.getMainLooper()).post {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000,
                            0f,
                            networkLocationListener
                        )
                    }
                }
            }

            var lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            lastKnownLocation.let {
                newLocation = lastKnownLocation!!
            }

            return newLocation
        }
    }


}