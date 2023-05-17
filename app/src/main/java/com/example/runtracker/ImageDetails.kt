package com.example.runtracker

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.io.File
import kotlin.math.abs

class ImageDetails : AppCompatActivity() {
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var filesPath  = ArrayList<String>()
    var filesSize = 0
    var currentPosition = 0
    lateinit var imageView : ImageView
    lateinit var leftButton : ImageButton
    lateinit var rightButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        imageView = findViewById(R.id.image)
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude",0.0)
        getPhotos()
        leftButton = findViewById(R.id.leftButton)
        rightButton = findViewById(R.id.rightButton)
        if(filesSize > 1){
            rightButton.visibility = View.VISIBLE
        }
        rightButton.setOnClickListener {
            currentPosition++
            uploadPhoto()
        }
        leftButton.setOnClickListener {
            currentPosition--;
            uploadPhoto()
        }
        uploadPhoto()

    }

    private fun uploadPhoto() {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myImageFile = File(directory, filesPath[currentPosition])
        setButtonsVisibility()

        Picasso.get().load(myImageFile ).into(imageView)
    }

    private fun setButtonsVisibility() {
        if(currentPosition>0) leftButton.visibility = View.VISIBLE
        else leftButton.visibility = View.INVISIBLE

        if(currentPosition<filesSize-1) rightButton.visibility = View.VISIBLE
        else rightButton.visibility = View.INVISIBLE


    }

    private fun getPhotos() {
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val files = directory.listFiles { file ->

            file.absolutePath.contains((abs(latitude)+abs(longitude)).toString().replace(".",""))
        }

        if (files != null) {
            filesSize = files.size
            for (file in files) {
                filesPath.add(file.name)
            }
        }

    }

}