package com.example.runtracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ColorPicker: AppCompatActivity() {

    var selectedColor : Int = -1
    lateinit var picker : com.larswerkman.holocolorpicker.ColorPicker

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color)
        picker = findViewById(R.id.picker)
        val saturationBar = findViewById<com.larswerkman.holocolorpicker.SaturationBar>(R.id.saturationbar)
        val valueBar = findViewById<com.larswerkman.holocolorpicker.ValueBar>(R.id.valuebar)
        picker.addValueBar(valueBar)
        picker.addSaturationBar(saturationBar)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            rememberContent()
        }

    }



    private fun rememberContent() {
        val newIntent = Intent()
        newIntent.putExtra("color", picker.color)
        setResult(Activity.RESULT_OK,newIntent)
        finish()
    }
}