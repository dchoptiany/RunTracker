package com.example.runtracker

import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import org.osmdroid.config.Configuration;
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.clans.fab.FloatingActionButton
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {
    lateinit var addPhotoFAButton : FloatingActionButton
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

        addPhotoFAButton = view!!.findViewById(R.id.addPhotoFAB) as FloatingActionButton
        addPhotoFAButton.setOnClickListener {
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

        val myGpsMyLocationProvider = GpsMyLocationProvider(activity)
        val myLocationOverlay = MyLocationNewOverlay(myGpsMyLocationProvider, mapView)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()

        val icon = BitmapFactory.decodeResource(resources, org.osmdroid.library.R.drawable.ic_menu_compass)
        myLocationOverlay.setPersonIcon(icon)
        mapView.overlays.add(myLocationOverlay)

        myLocationOverlay.runOnFirstFix {
            mapView.overlays.clear()
            mapView.overlays.add(myLocationOverlay)
            mapController.animateTo(myLocationOverlay.myLocation)
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
}