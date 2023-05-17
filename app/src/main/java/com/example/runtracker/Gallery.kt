package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.GalleryAdapter
import java.io.File
import java.io.FileOutputStream

class Gallery : AppCompatActivity(), GalleryAdapter.OnImageClickListeren {
    lateinit var rv : RecyclerView
    lateinit var  sharedPreferencesSettings : SharedPreferences
    var images = ArrayList<Images>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        rv = findViewById(R.id.rv)
        sharedPreferencesSettings = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setAppAppearance()
        addUserPhoto()
        setView()
    }


    fun setView(){
        rv = findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(applicationContext,2)
        rv.adapter = GalleryAdapter(images,this)
    }

    private fun setAppAppearance(){
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<ConstraintLayout>(R.id.main)
        if(sharedPreferencesSettings.getBoolean("darkMode", false)){

            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170,170,170))
        }
        else {
            background.setBackgroundColor(Color.rgb(255,255,255))
            header.setBackgroundColor(sharedPreferencesSettings.getInt("color", Color.BLACK))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addUserPhoto(){
        val cw = ContextWrapper(this)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val files = directory.list()
        for (fileName in files!!) {
            var found = false
            for (i in images) {
                if (i.path == fileName) {
                    found = true
                    break
                }
            }
            if (!found) {
                images.add(Images(fileName, ""))
            }
        }
    }

    override fun onImgClick(position: Int) {
        val intent = Intent(this,GalleryImageDetails::class.java)
        intent.putExtra("path",images[position].path)
        startActivity(intent)
    }
}