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
        const val SIGNIFICANT_TIME_DIFFERENCE: Long = 120000L // 2 minutes in milliseconds
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

            var newLocationNetwork : Location? = null
            var newLocationGPS : Location? = null
            var locationManagerGPS = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var locationManagerNetwork = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var hasNetwork = locationManagerNetwork.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            var hasGPS = locationManagerNetwork.isProviderEnabled(LocationManager.GPS_PROVIDER)


            var networkLocationListener : LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    newLocationNetwork = location
                }
            }

            var GPSLocationListener : LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    newLocationGPS = location
                }
            }

            if(hasNetwork) {
                if(ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Handler(Looper.getMainLooper()).post {
                        locationManagerNetwork.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0,
                            0f,
                            networkLocationListener
                        )
                    }
                }
            }

            if(hasGPS) {
                if(ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Handler(Looper.getMainLooper()).post {
                        locationManagerGPS.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0f,
                            GPSLocationListener
                        )
                    }
                }
            }

            var lastKnownLocationNetwork = locationManagerNetwork.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            var lastKnownLocationGPS = locationManagerNetwork.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var lastKnownLocation : Location? = null
            when (isBetterLocation(lastKnownLocationGPS, lastKnownLocationNetwork)) {
                true -> lastKnownLocation = lastKnownLocationGPS
                false -> lastKnownLocation = lastKnownLocationNetwork
            }

            return lastKnownLocation!!
        }

        fun isBetterLocation(location : Location?, currBestLocation : Location?) : Boolean{
            if(currBestLocation == null) {
                return true
            }

            if(location == null) {
                return false
            }

            // check whether the new location fix is newer or older
            val timeDelta : Long = location.time - currBestLocation.time
            val isSignificantlyNewer: Boolean = timeDelta > SIGNIFICANT_TIME_DIFFERENCE
            val isSignificantlyOlder:Boolean = timeDelta < -SIGNIFICANT_TIME_DIFFERENCE

            when {
                // if it's been more than two minutes since the current location, use the new location because the user has likely moved
                isSignificantlyNewer -> return true
                // if the new location is more than two minutes older, it must be worse
                isSignificantlyOlder -> return false
            }

            // check whether the new location fix is more or less accurate
            val isNewer: Boolean = timeDelta > 0L
            val accuracyDelta: Float = location.accuracy - currBestLocation.accuracy
            val isLessAccurate: Boolean = accuracyDelta > 0f
            val isMoreAccurate: Boolean = accuracyDelta < 0f
            val isSignificantlyLessAccurate: Boolean = accuracyDelta > 200f

            // check if the old and new location are from the same provider
            val isFromSameProvider: Boolean = location.provider == currBestLocation.provider

            // determine location quality using a combination of timeliness and accuracy
            return when {
                isMoreAccurate -> true
                isNewer && !isLessAccurate -> true
                isNewer && !isSignificantlyLessAccurate && isFromSameProvider -> true
                else -> false
            }
        }
    }
}