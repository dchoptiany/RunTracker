package com.example.runtracker.settings

import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R


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
