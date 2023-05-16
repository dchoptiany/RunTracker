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


    init{
        view.setOnClickListener(this)


    }



    override fun onClick(v: View?) {

        onBlockListener.onBlockClick(adapterPosition)

    }
}
