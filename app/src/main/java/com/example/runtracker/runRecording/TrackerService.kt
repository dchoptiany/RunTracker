package com.example.runtracker.runRecording

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
import com.example.runtracker.database.Run
import org.osmdroid.util.GeoPoint
import java.sql.Date
import java.util.*

class TrackerService : Service() {
    companion object {
        const val TRACKER_UPDATED = "trackerUpdated"
        const val DIST_EXTRA = "distExtra"
        const val LAT_EXTRA = "latitudeExtra"
        const val LON_EXTRA = "longitudeExtra"
    }

    private val timer = Timer()

    private lateinit var currentLocation: GeoPoint
    private var latitude = 0.0
    private var longitude = 0.0

    private lateinit var run: Run
    private var duration: Int = 0
    private var distance: Float = 0f
    private var points: MutableList<GeoPoint> = mutableListOf()

    private lateinit var startingTime: Date
    private lateinit var stoppedTime: Date

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

    private fun startTracking() {
        // get "run start" time
        var starting = Calendar.getInstance().time
        startingTime = Date(starting.time)

        // start recording location changes
        timer.scheduleAtFixedRate(DistanceTask(distance), 0, 1000)
    }

    private fun stopTracking() {
        // get "run stop" time
        val stopped = Calendar.getInstance().time
        stoppedTime = Date(stopped.time)

        // calc "run duration"
        duration = (stoppedTime.time - startingTime.time).toInt()

        // create run object based on collected data
        run = Run(0, startingTime, distance, duration, points)

        //TODO: save run object in database

        // stop recording location changes
        timer.cancel()
    }

    private inner class DistanceTask(private var distance: Float) : TimerTask() {
        override fun run() {
            // add way point
            val runDataPair: Pair<Float, MutableList<GeoPoint>> =
                RunHelper.addWayPointToRun(points, distance, currentLocation)
            distance = runDataPair.first
            points = runDataPair.second

            // get curr location
            val newLocation = getLocation()
            currentLocation = GeoPoint(newLocation.latitude, newLocation.longitude)

            // send data to MapFragment
            val intent = Intent(TRACKER_UPDATED)
            intent.putExtra(LAT_EXTRA, newLocation.latitude)
            intent.putExtra(LON_EXTRA, newLocation.longitude)
            intent.putExtra(DIST_EXTRA, distance)
            sendBroadcast(intent)
        }

        fun getLocation(): Location {
            var newLocation = Location(LocationManager.NETWORK_PROVIDER)
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            val networkLocationListener = LocationListener { location -> newLocation = location }

            if (hasNetwork) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
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

            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            lastKnownLocation.let {
                newLocation = lastKnownLocation!!
            }

            return newLocation
        }
    }
}