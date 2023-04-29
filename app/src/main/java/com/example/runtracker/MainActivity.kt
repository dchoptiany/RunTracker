package com.example.runtracker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MenuAdapter.OnBlockClickListener {
    lateinit var rv : RecyclerView
    var menuItems : ArrayList<MenuItem> = ArrayList()

    private val runViewModel: RunViewModel by viewModels {
        RunModelFactory((application as RunApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addMenuItems()
        setView()
    }

    fun setView(){
        rv = findViewById(R.id.rv)
        rv.layoutManager = GridLayoutManager(applicationContext,2)
        rv.adapter = MenuAdapter(menuItems,this)
    }

    fun addMenuItems(){
        addtoMenuItemsList("ACTIVITIES",R.drawable.activites)
        addtoMenuItemsList("GALLERY",R.drawable.aparat)
        addtoMenuItemsList("STATISTICS",R.drawable.statistics)
        addtoMenuItemsList("MY ACCOUNT",R.drawable.account)
        addtoMenuItemsList("ACHIEVEMENTS",R.drawable.achievements)
        addtoMenuItemsList("SETTINGS",R.drawable.settings)
    }

    fun addtoMenuItemsList(data : String, drawable : Int){
        val menuItem = MenuItem(data,drawable)
        menuItems.add(menuItem)
    }

    override fun onBlockClick(position: Int, date: String) {
        TODO("Not yet implemented")
    }

}
