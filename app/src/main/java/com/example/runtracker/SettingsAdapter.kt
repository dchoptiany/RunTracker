package com.example.runtracker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SettingsAdapter(var settingsItems : ArrayList<settingsItem>,var  onBlockListener : OnSettingsItemClickListener): RecyclerView.Adapter<SettingsViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.settings_item,parent,false)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            parent.height /7
        )
        view.layoutParams = layoutParams
        return SettingsViewHolder(view,onBlockListener)
    }

    override fun getItemCount(): Int {
        return settingsItems.size
    }

    @SuppressLint("CommitPrefEdits")
    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val sharedPreferences = holder.view.context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        holder.text.text = settingsItems[position].text
        holder.icon.setImageResource(settingsItems[position].icon)
        if(sharedPreferences.getBoolean("darkMode",false)){
            holder.switch.isChecked = true
        }
        if(!settingsItems[position].showSwith){
            holder.switch.visibility = View.INVISIBLE
        }
        else{
            holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
                when(position){

                    1 -> {
                        editor.putBoolean("darkMode",isChecked)
                        editor.apply()
                    }
                    2 -> {
                        editor.putBoolean("notifications",isChecked)
                        editor.apply()
                    }

                }
            }
        }
    }

    interface OnSettingsItemClickListener {
        fun onBlockClick(position: Int, date: String)

    }
}