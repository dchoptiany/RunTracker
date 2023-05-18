package com.example.runtracker.menu

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R

class MenuViewHolder(val view: View, private var onBlockListener: MenuAdapter.OnBlockClickListener)
    : RecyclerView.ViewHolder(view),View.OnClickListener {

    var textView: TextView = view.findViewById(R.id.text)
    var imageView: ImageView = view.findViewById(R.id.image)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onBlockListener.onBlockClick(adapterPosition, "")
    }
}