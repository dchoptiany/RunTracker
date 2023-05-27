package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.gallery.GalleryActivity
import com.example.runtracker.history.HistoryActivity
import com.example.runtracker.menu.MenuAdapter
import com.example.runtracker.menu.MenuItem
import com.example.runtracker.myAccount.MyAccountActivity
import com.example.runtracker.runRecording.MapActivity
import com.example.runtracker.settings.SettingsActivity
import com.example.runtracker.statistics.StatisticsActivity

class MainActivity : AppCompatActivity(), MenuAdapter.OnBlockClickListener {
    private lateinit var recyclerViewMenuItems: RecyclerView
    private var menuItems: ArrayList<MenuItem> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val title = findViewById<TextView>(R.id.text)
        title.text = "Main menu"
        addMenuItems()
        setView()
    }

    private fun setView() {
        recyclerViewMenuItems = findViewById(R.id.recyclerViewMenuItems)
        recyclerViewMenuItems.layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerViewMenuItems.adapter = MenuAdapter(menuItems, this)
    }

    private fun addMenuItems() {
        addToMenuItemsList("START ACTIVITY", R.drawable.activites)
        addToMenuItemsList("GALLERY", R.drawable.camera)
        addToMenuItemsList("STATISTICS", R.drawable.statistics)
        addToMenuItemsList("MY ACCOUNT", R.drawable.account)
        addToMenuItemsList("HISTORY", R.drawable.history)
        addToMenuItemsList("SETTINGS", R.drawable.settings)
    }

    private fun addToMenuItemsList(data: String, drawable: Int) {
        menuItems.add(MenuItem(data, drawable))
    }

    override fun onBlockClick(position: Int, date: String) {
        when (position) {
            0 -> {
                Intent(this, MapActivity::class.java).also {
                    if(isLocationPermissionGranted()) {
                        Log.i("mymap", "Localization permission granted")
                        startActivity(it)
                    }
                }
            }
            1 -> {
                Intent(this, GalleryActivity::class.java).also {
                    startActivity(it)
                }
            }
            2 -> {
                Intent(this, StatisticsActivity::class.java).also {
                    startActivity(it)
                }
            }
            3 -> {
                Intent(this, MyAccountActivity::class.java).also {
                    startActivity(it)
                }
            }
            4 -> {
                Intent(this, HistoryActivity::class.java).also {
                    startActivity(it)
                }
            }
            5 -> {
                Intent(this, SettingsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        val background = findViewById<ConstraintLayout>(R.id.constraint)
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        if (sharedPreferences.getBoolean("darkMode", false)) {
            val newColor = Color.rgb(170, 170, 170)
            background.setBackgroundColor(newColor)
            header.setBackgroundColor(Color.BLACK)
        } else {
            header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
            background.setBackgroundColor(Color.WHITE)
        }
    }


    private fun isLocationPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return false
        } else {
            return true
        }
    }

}
