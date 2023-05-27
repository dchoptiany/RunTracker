package com.example.runtracker.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnImageClickListener {
    private lateinit var recyclerViewImages: RecyclerView
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private var images = ArrayList<Images>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val title = findViewById<TextView>(R.id.text)
        title.text = "Gallery"
        recyclerViewImages = findViewById(R.id.recyclerViewImages)
        sharedPreferencesSettings = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setAppAppearance()
        addUserPhoto()
        setView()
    }

    private fun setView() {
        recyclerViewImages = findViewById(R.id.recyclerViewImages)
        recyclerViewImages.layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerViewImages.adapter = GalleryAdapter(images, this)
    }

    private fun setAppAppearance() {
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<ConstraintLayout>(R.id.main)
        if (sharedPreferencesSettings.getBoolean("darkMode", false)) {

            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        } else {
            background.setBackgroundColor(Color.rgb(255, 255, 255))
            header.setBackgroundColor(sharedPreferencesSettings.getInt("color", Color.BLACK))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addUserPhoto() {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val subDirectories = directory.listFiles { file -> file.isDirectory }

        for (subDirectory in subDirectories!!) {
            val files = subDirectory.listFiles { file -> file.isFile } ?: continue

            for (file in files) {
                val fileName = file.name
                val found = images.any { image -> image.path == fileName }
                if (!found) {
                    images.add(Images(fileName, ""))
                }
            }
        }
    }

    override fun onImageClick(position: Int) {
        val intent = Intent(this, GalleryImageDetailsActivity::class.java)
        intent.putExtra("path", images[position].path)
        startActivity(intent)
    }
}