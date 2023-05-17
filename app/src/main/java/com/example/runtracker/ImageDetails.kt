package com.example.runtracker

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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

    lateinit var  sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        imageView = findViewById(R.id.image)
        sharedPreferences = getSharedPreferences("settings",Context.MODE_PRIVATE)
        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude",0.0)
        setAppAppearance()

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

    private fun setAppAppearance(){
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<ConstraintLayout>(R.id.main)
        if(sharedPreferences.getBoolean("darkMode", false)){

            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170,170,170))
        }
        else {
            background.setBackgroundColor(Color.rgb(255,255,255))
            header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
        }
    }

}