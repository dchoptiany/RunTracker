package com.example.runtracker.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R
import com.example.runtracker.statistics.StringFormatter

class RunHistoryAdapter(private val runItems: ArrayList<RunHistoryItem>,
                        private var onRunItemClickListener: OnRunItemClickListener)
    : RecyclerView.Adapter<RunHistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunHistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.run_item, parent, false)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams = layoutParams
        return RunHistoryItemViewHolder(view, onRunItemClickListener)
    }

    override fun getItemCount(): Int {
        return runItems.size
    }

    override fun onBindViewHolder(holder: RunHistoryItemViewHolder, position: Int) {
        holder.runID = runItems[position].getRunID()
        holder.textViewName.text = runItems[position].getName()
        holder.textViewDistance.text =  StringFormatter.getInstance().formatDistance(runItems[position].distance)
        holder.textViewDuration.text =  StringFormatter.getInstance().formatTime(runItems[position].duration)
    }

    interface OnRunItemClickListener {
        fun onClick(position: Int, runID: Int)
    }
}