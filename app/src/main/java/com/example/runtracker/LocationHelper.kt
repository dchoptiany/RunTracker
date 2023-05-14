package com.example.runtracker

import android.location.Location
import android.location.LocationManager
import android.util.Log
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

object LocationHelper {
    fun getLastKnownLocation(myLocationOverlay : MyLocationNewOverlay) : GeoPoint {
        var location : GeoPoint = myLocationOverlay.myLocation
        return location
    }

    fun calcDistance(previousLocation : GeoPoint, location : GeoPoint) : Float {
        var prev : Location = Location(LocationManager.GPS_PROVIDER)
        prev.latitude = previousLocation.latitude
        prev.longitude = previousLocation.longitude

        var curr : Location = Location(LocationManager.GPS_PROVIDER)
        curr.latitude = location.latitude
        curr.longitude = location.longitude

        Log.i("mytracker", "CALC PREV LAT:" + prev.latitude + " LON: " + prev.longitude + " CURR LAT: " + curr.latitude.toString() + " LON: " + curr.longitude.toString())
        var distance = prev.distanceTo(curr)
        Log.i("mytracker", "CALC DIST: " + distance)
        return distance
    }
}