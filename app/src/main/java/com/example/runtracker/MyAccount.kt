package com.example.runtracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyAccount : AppCompatActivity() {
    lateinit var  sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var rv : RecyclerView
    var myAccountItems = ArrayList<MyAccountItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        addMenuItems()
        setView()
    }

    fun setView(){
        rv = findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(applicationContext,2)
        rv.adapter = MyAccountAdapter(myAccountItems)
    }

    fun addMenuItems(){
        addtoAccountItemsList("Name", "input")
        addtoAccountItemsList("Surname","input")
        addtoAccountItemsList("Sex","spinner")
        addtoAccountItemsList("Date of birth","calendar")
        addtoAccountItemsList("Weigh","input")
    }

    fun addtoAccountItemsList(data : String, appWidget : String){
        val menuItem = MyAccountItem(data,appWidget)
        myAccountItems.add(menuItem)
    }
}