package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.GalleryAdapter
import java.io.File
import java.io.FileOutputStream

class Gallery : AppCompatActivity(), GalleryAdapter.OnImageClickListeren {
    lateinit var rv : RecyclerView
    lateinit var  sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var images = ArrayList<Images>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        addUserPhoto()
        setView()
    }


    fun setView(){
        rv = findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(applicationContext,2)
        rv.adapter = GalleryAdapter(images,this)
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
                rv.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onImgClick(position: Int) {
        TODO("Not yet implemented")
    }
}