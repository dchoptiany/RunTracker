package com.example.runtracker.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.runtracker.R

class ColorPickerActivity: AppCompatActivity() {
    private lateinit var picker: com.larswerkman.holocolorpicker.ColorPicker
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val title = findViewById<TextView>(R.id.text)
        title.text = "Color picker"
        picker = findViewById(R.id.picker)
        val saturationBar =
            findViewById<com.larswerkman.holocolorpicker.SaturationBar>(R.id.saturationbar)
        val valueBar = findViewById<com.larswerkman.holocolorpicker.ValueBar>(R.id.valuebar)
        picker.oldCenterColor = sharedPreferences.getInt("color", Color.BLACK)
        picker.addValueBar(valueBar)
        picker.addSaturationBar(saturationBar)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            rememberContent()
        }
        setAppAppearance()
    }

    private fun rememberContent() {
        val newIntent = Intent()
        newIntent.putExtra("color", picker.color)
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun setAppAppearance() {
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<LinearLayout>(R.id.main)
        if (sharedPreferences.getBoolean("darkMode", false)) {
            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        } else {
            background.setBackgroundColor(Color.rgb(255, 255, 255))
            header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
        }
    }
}
