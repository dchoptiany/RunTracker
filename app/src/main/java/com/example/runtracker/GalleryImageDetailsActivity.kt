package com.example.runtracker

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.io.File

class GalleryImageDetailsActivity: AppCompatActivity() {
    private lateinit var imagePath: String
    private lateinit var imageView: ImageView
    private lateinit var sharedPreferencesSettings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_image_details)
        imageView = findViewById(R.id.imageView)
        sharedPreferencesSettings = getSharedPreferences("settings", Context.MODE_PRIVATE)
        imagePath = intent.getStringExtra("path").toString()
        setAppAppearance()
        uploadPhoto()
    }

    private fun uploadPhoto() {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myImageFile = File(directory, imagePath)

        Picasso.get().load(myImageFile).into(imageView)
    }

    private fun setAppAppearance() {
        val background = findViewById<ConstraintLayout>(R.id.main)
        if (sharedPreferencesSettings.getBoolean("darkMode", false)) {
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        } else {
            background.setBackgroundColor(Color.rgb(255, 255, 255))
        }
    }
}