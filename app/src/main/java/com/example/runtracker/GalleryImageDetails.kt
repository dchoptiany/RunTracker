package com.example.runtracker

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.io.File

class GalleryImageDetails: AppCompatActivity() {

    lateinit var path : String
    lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_image_details)
        imageView = findViewById(R.id.imageView)
        path = intent.getStringExtra("path").toString()
        uploadPhoto()


    }

    private fun uploadPhoto() {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myImageFile = File(directory, path)

        Picasso.get().load(myImageFile ).into(imageView)
    }
}