package com.example.runtracker

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(var menuItems : ArrayList<MenuItem>,var  onBlockListener : OnBlockClickListener): RecyclerView.Adapter<MenuViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item,parent,false)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            parent.height / 3
        )
        view.layoutParams = layoutParams
        return MenuViewHolder(view,onBlockListener)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.text.text = menuItems[position].menuItemData
        holder.image.setImageResource(menuItems[position].menuItemDarwable)
    }

    interface OnBlockClickListener {
        fun onBlockClick(position: Int, date: String)

    }
}