package com.example.runtracker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RunHistoryItemViewHolder(view: View, private val onRunItemClickListener: RunHistoryAdapter.OnRunItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var runID: Int = -1
    val textViewName: TextView = view.findViewById(R.id.textViewRunItemName)
    val textViewDistance:TextView = view.findViewById(R.id.textViewRunItemDistance)
    val textViewDuration: TextView = view.findViewById(R.id.textViewRunItemDuration)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onRunItemClickListener.onClick(adapterPosition, runID)
    }
}