package com.example.runtracker.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.ColorPickerActivity
import com.example.runtracker.R
import com.example.runtracker.settingsItem

class SettingsActivity :  AppCompatActivity() , SettingsAdapter.OnSettingsItemClickListener {
    private lateinit var recyclerViewSettings: RecyclerView
    private var settingsItems = ArrayList<settingsItem>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            rememberContent()
        }
        addToSettingsItems()
        initRecyclerView()
    }

    private fun rememberContent() {
        val newIntent = Intent()
        newIntent.putExtra("color", sharedPreferences.getInt("color", Color.BLACK))
        newIntent.putExtra("notifications", sharedPreferences.getBoolean("notifications", false))
        newIntent.putExtra("darkMode", sharedPreferences.getBoolean("darkMode", false))
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    private fun initRecyclerView() {
        recyclerViewSettings = findViewById(R.id.recyclerViewSettings)
        recyclerViewSettings.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerViewSettings.adapter = SettingsAdapter(settingsItems, this)
    }

    private fun addToSettingsItems() {
        val itemAppColor = settingsItem("Set app color", R.drawable.color_picker, false)
        val itemDarkMode = settingsItem("Dark mode", R.drawable.dark_mode, true)
        val itemNotifications = settingsItem("Notifications", R.drawable.notifications, true)
        settingsItems.add(itemAppColor)
        settingsItems.add(itemDarkMode)
        settingsItems.add(itemNotifications)
    }

    private fun setDarkMode() {
        if (sharedPreferences.getBoolean("darkMode", false)) {
            val header = findViewById<LinearLayout>(R.id.linearLayout)
            val background = findViewById<ConstraintLayout>(R.id.main)
            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        }
    }

    private fun setAppAppearance() {
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<ConstraintLayout>(R.id.main)
        if (sharedPreferences.getBoolean("darkMode", false)) {
            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        } else {
            background.setBackgroundColor(Color.rgb(255, 255, 255))
            header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBlockClick(position: Int) {
        if (position == 0) {
            val colorIntent = Intent(this, ColorPickerActivity::class.java)
            resultLauncher.launch(colorIntent)
        }
        setAppAppearance()
        recyclerViewSettings.adapter?.notifyDataSetChanged()
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (data != null) {
                data.getIntExtra("color", Color.BLACK).let {
                    editor.putInt("color", it)
                    editor.apply()
                }
                val header = findViewById<LinearLayout>(R.id.linearLayout)
                header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
            }
        }

    override fun onResume() {
        super.onResume()
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
        setDarkMode()
    }
}