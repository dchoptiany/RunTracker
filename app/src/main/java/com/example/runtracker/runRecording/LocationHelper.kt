package com.example.runtracker.runRecording

import android.location.Location
import android.location.LocationManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

object LocationHelper {
    fun getLastKnownLocation(myLocationOverlay: MyLocationNewOverlay): GeoPoint {
        return myLocationOverlay.myLocation
    }

    fun calcDistance(previousLocation: GeoPoint, location: GeoPoint): Float {
        val prev = Location(LocationManager.GPS_PROVIDER)
        prev.latitude = previousLocation.latitude
        prev.longitude = previousLocation.longitude

        val curr = Location(LocationManager.GPS_PROVIDER)
        curr.latitude = location.latitude
        curr.longitude = location.longitude

        return prev.distanceTo(curr)
    }
}