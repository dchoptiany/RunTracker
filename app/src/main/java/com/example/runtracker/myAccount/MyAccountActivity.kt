package com.example.runtracker.myAccount

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
import com.example.runtracker.R

class MyAccountActivity : AppCompatActivity(), MyAccountAdapter.OnMyAccountClickListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesSettings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var recyclerViewAccount: RecyclerView
    private var myAccountItems = ArrayList<MyAccountItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        sharedPreferences = getSharedPreferences("my_account", Context.MODE_PRIVATE)
        sharedPreferencesSettings = getSharedPreferences("settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        addMenuItems()
        setView()
        setAppAppearance()
        val backButton = findViewById<Button>(R.id.button)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setView() {
        recyclerViewAccount = findViewById(R.id.recyclerViewAccount)
        recyclerViewAccount.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerViewAccount.adapter = MyAccountAdapter(myAccountItems, this)
    }

    private fun setAppAppearance() {
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<ConstraintLayout>(R.id.main)
        if (sharedPreferencesSettings.getBoolean("darkMode", false)) {
            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        } else {
            background.setBackgroundColor(Color.rgb(255, 255, 255))
            header.setBackgroundColor(sharedPreferencesSettings.getInt("color", Color.BLACK))
        }
    }

    private fun addMenuItems() {
        addToAccountItemsList("Name", "input")
        addToAccountItemsList("Surname", "input")
        addToAccountItemsList("Sex", "spinner")
        addToAccountItemsList("Date of birth", "calendar")
        addToAccountItemsList("Weight", "input")
        addToAccountItemsList("Height", "input")
    }

    private fun addToAccountItemsList(data: String, appWidget: String) {
        val menuItem = MyAccountItem(data, appWidget)
        myAccountItems.add(menuItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBlockClick(position: Int) {
        recyclerViewAccount.adapter?.notifyDataSetChanged()
    }
}
