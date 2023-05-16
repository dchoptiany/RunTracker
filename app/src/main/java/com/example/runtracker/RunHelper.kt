package com.example.runtracker

import org.osmdroid.util.GeoPoint

object RunHelper {
    fun addWayPointToRun(wayPoints : MutableList<GeoPoint>, distance: Float, location : GeoPoint) : Pair<Float, MutableList<GeoPoint>> {
        val previousLocation : GeoPoint?
        var numOfWayPoints : Int = wayPoints.size
        var newDistance : Float = 0f

        // check if point is "way point"
        // CASE1: First location
        if(numOfWayPoints == 0) {
            previousLocation = null
        } else { // CASE2: "Middle" location
            previousLocation = wayPoints[numOfWayPoints-1]
            newDistance = distance + LocationHelper.calcDistance(previousLocation, location)
        }

        var point = GeoPoint(location.latitude, location.longitude)
        wayPoints.add(point)

        var result : Pair<Float, MutableList<GeoPoint>> = Pair(newDistance, wayPoints)

        return result
    }
}