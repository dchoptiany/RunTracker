package com.example.runtracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Settings :  AppCompatActivity() , SettingsAdapter.OnSettingsItemClickListener{

    lateinit var rv : RecyclerView
    var settingsItems = ArrayList<settingsItem>()
    lateinit var  sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        sharedPreferences = getSharedPreferences("settings",Context.MODE_PRIVATE)
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
        newIntent.putExtra("color", sharedPreferences.getInt("color",Color.BLACK))
        newIntent.putExtra("notifications", sharedPreferences.getBoolean("notifications",false))
        newIntent.putExtra("darkMode", sharedPreferences.getBoolean("darkMode",false))
        setResult(Activity.RESULT_OK,newIntent)
        finish()
    }

    private fun initRecyclerView() {
        rv = findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(applicationContext,1)
        rv.adapter = SettingsAdapter(settingsItems,this)
    }

    fun addToSettingsItems(){
        val item1 = settingsItem("Set app color",R.drawable.color_picker,false)
        val item2 = settingsItem("Dark mode",R.drawable.dark_mode,true)
        val item3 = settingsItem("Notifications" , R.drawable.notifications,true)
        settingsItems.add(item1)
        settingsItems.add(item2)
        settingsItems.add(item3)
    }


    override fun onBlockClick(position: Int, date: String) {
        val colorIntent = Intent(this,ColorPicker::class.java)
        resultLauncher.launch(colorIntent)
    }

    var resultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result-> val data = result.data
        if (data != null) {
            data.getIntExtra("color",Color.BLACK).let {
                editor.putInt("color",it)
                editor.apply()
            }
            val header = findViewById<LinearLayout>(R.id.linearLayout)
            header.setBackgroundColor(sharedPreferences.getInt("color",Color.BLACK))
        }
    }

    override fun onResume() {
        super.onResume()
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        header.setBackgroundColor(sharedPreferences.getInt("color",Color.BLACK))
    }
}