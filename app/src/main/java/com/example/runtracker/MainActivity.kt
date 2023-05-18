package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), MenuAdapter.OnBlockClickListener {
    lateinit var rv : RecyclerView
    var menuItems : ArrayList<MenuItem> = ArrayList()
    lateinit var  sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor

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
        rv.adapter = MenuAdapter(menuItems,this)
    }

    fun addMenuItems(){
        addtoMenuItemsList("START ACTIVITY",R.drawable.activites)
        addtoMenuItemsList("GALLERY",R.drawable.aparat)
        addtoMenuItemsList("STATISTICS",R.drawable.statistics)
        addtoMenuItemsList("MY ACCOUNT",R.drawable.account)
        addtoMenuItemsList("HISTORY",R.drawable.history)
        addtoMenuItemsList("SETTINGS",R.drawable.settings)
    }

    fun addtoMenuItemsList(data : String, drawable : Int){
        val menuItem = MenuItem(data,drawable)
        menuItems.add(menuItem)
    }

    override fun onBlockClick(position: Int, date: String) {
        when(position) {
            0 -> {
                val mapIntent = Intent(this, MapActivity::class.java)
                this.startActivity(mapIntent)
                }
            1 -> Toast.makeText(this,"2",Toast.LENGTH_SHORT).show()
            2-> Toast.makeText(this,"3",Toast.LENGTH_SHORT).show()
            3-> {
                val myAccount = Intent(this, MyAccount::class.java)
                resultLauncher.launch(myAccount)
            }
            4-> {
                Intent(this, HistoryActivity::class.java).also {
                    startActivity(it)
                }
            }
            5-> {
                val settings = Intent(this, Settings::class.java)
                resultLauncher.launch(settings)
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        val background = findViewById<ConstraintLayout>(R.id.constraint)
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        if(sharedPreferences.getBoolean("darkMode",false)){
            var newColor = Color.rgb(170,170,170)
            background.setBackgroundColor(newColor)
            header.setBackgroundColor(Color.BLACK)

        }
        else{
            header.setBackgroundColor(sharedPreferences.getInt("color",Color.BLACK))
            background.setBackgroundColor(Color.WHITE)
        }

    }


    var resultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result-> val data = result.data
        if (data != null) {
            data.getBooleanExtra("darkMode",false).let { editor.putBoolean("darkMode",it)
            editor.apply()
            }
            data.getBooleanExtra("notifications",false).let { editor.putBoolean("notifications",it)
            editor.apply()}
            data.getIntExtra("color",Color.BLACK).let { editor.putInt("color",it)
            editor.apply()}
        }
    }

}
