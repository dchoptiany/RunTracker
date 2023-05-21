package com.example.runtracker.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R

class MenuAdapter(private var menuItems: ArrayList<MenuItem>, private var onBlockListener: OnBlockClickListener)
    : RecyclerView.Adapter<MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            parent.height / 3
        )
        view.layoutParams = layoutParams
        return MenuViewHolder(view, onBlockListener)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.textView.text = menuItems[position].data
        holder.imageView.setImageResource(menuItems[position].drawable)
    }

    interface OnBlockClickListener {
        fun onBlockClick(position: Int, date: String)
    }
}