package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyAccount : AppCompatActivity(), MyAccountAdapter.OnMyAccountClickListener {
    private lateinit var  sharedPreferences : SharedPreferences
    private lateinit var sharedPreferencesSettings : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var rv : RecyclerView
    var myAccountItems = ArrayList<MyAccountItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        sharedPreferences = getSharedPreferences("my_account", Context.MODE_PRIVATE)
        sharedPreferencesSettings = getSharedPreferences("settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        addMenuItems()
        setView()
        setAppAppearence()
        val backButton = findViewById<Button>(R.id.button)
        backButton.setOnClickListener {
            finish()
        }
    }

    fun setView(){
        rv = findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(applicationContext,1)
        rv.adapter = MyAccountAdapter(myAccountItems,this)
    }

    private fun setAppAppearence(){
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

    fun addMenuItems(){
        addtoAccountItemsList("Name", "input")
        addtoAccountItemsList("Surname","input")
        addtoAccountItemsList("Sex","spinner")
        addtoAccountItemsList("Date of birth","calendar")
        addtoAccountItemsList("Weigh","input")
    }

    private fun addtoAccountItemsList(data : String, appWidget : String){
        val menuItem = MyAccountItem(data,appWidget)
        myAccountItems.add(menuItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBlockClick(position: Int) {
        rv.adapter?.notifyDataSetChanged()
    }
}