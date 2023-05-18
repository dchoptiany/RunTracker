package com.example.runtracker.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.runtracker.R

class ColorPickerActivity: AppCompatActivity() {
    private lateinit var picker: com.larswerkman.holocolorpicker.ColorPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)
        picker = findViewById(R.id.picker)
        val saturationBar =
            findViewById<com.larswerkman.holocolorpicker.SaturationBar>(R.id.saturationbar)
        val valueBar = findViewById<com.larswerkman.holocolorpicker.ValueBar>(R.id.valuebar)
        picker.addValueBar(valueBar)
        picker.addSaturationBar(saturationBar)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            rememberContent()
        }
    }

    private fun rememberContent() {
        val newIntent = Intent()
        newIntent.putExtra("color", picker.color)
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }
}
