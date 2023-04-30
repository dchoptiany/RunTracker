package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.larswerkman.holocolorpicker.ColorPicker


class SettingsViewHolder(val view : View,var onBlockListener : SettingsAdapter.OnSettingsItemClickListener) : RecyclerView.ViewHolder(view),View.OnClickListener {

    var text  = view.findViewById<TextView>(R.id.dataText)
    var icon = view.findViewById<ImageView>(R.id.icon)
    var switch = view.findViewById<Switch>(R.id.switch1)
    var picker = view.findViewById<ColorPicker>(R.id.picker)
    var selectedColor = Color.WHITE

    init{
        view.setOnClickListener(this)


    }



    override fun onClick(v: View?) {
        if(adapterPosition==0) {
            onBlockListener.onBlockClick(adapterPosition, "")
        }
        //Toast.makeText(view.context,text.text,Toast.LENGTH_SHORT).show()
//
        //// temporary test for Map Fragment
        //if(text.text.equals("ACTIVITIES")) {
        //    var mapIntent = Intent(view.context, MapActivity::class.java)
        //    view.context.startActivity(mapIntent)
        //}
    }
}