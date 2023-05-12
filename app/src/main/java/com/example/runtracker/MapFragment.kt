package com.example.runtracker

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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


class MapFragment : Fragment() {
    lateinit var addPhotoButton : FloatingActionButton
    lateinit var startButton : ImageButton
    lateinit var pauseButton : ImageButton
    lateinit var stopButton : ImageButton
    lateinit var mapView : MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_map, container, false)

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

        val mapController : IMapController = mapView.controller
        mapController.zoomTo(14, 1)

        var defaultLocation : GeoPoint = GeoPoint(51.1077,17.0625) // Wrocław University of Science and Technlogy
        mapController.animateTo(defaultLocation)

        // set current location
        if(isLocationPermissionGranted()) {
            Log.i("mymap", "Localization permission granted")

            val myGpsMyLocationProvider = GpsMyLocationProvider(activity)
            val myLocationOverlay = MyLocationNewOverlay(myGpsMyLocationProvider, mapView)
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

        return view
    }

    fun addPhoto() {
        Toast.makeText(requireContext(), "Add Photo!", Toast.LENGTH_SHORT).show()
    }

    fun startActivity() {
        Toast.makeText(requireContext(), "Start Running!", Toast.LENGTH_SHORT).show()
    }

    fun pauseActivity() {
        Toast.makeText(requireContext(), "Running paused!", Toast.LENGTH_SHORT).show()
    }

    fun stopActivity() {
        Toast.makeText(requireContext(), "Running stopped!", Toast.LENGTH_SHORT).show()
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