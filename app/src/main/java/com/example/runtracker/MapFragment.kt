package com.example.runtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.clans.fab.FloatingActionButton

class MapFragment : Fragment() {
    lateinit var addPhotoFAButton : FloatingActionButton
    lateinit var startButton : ImageButton
    lateinit var pauseButton : ImageButton
    lateinit var stopButton : ImageButton

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