package com.example.runtracker.history

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.*
import com.example.runtracker.database.Run
import com.example.runtracker.database.RunModelFactory
import com.example.runtracker.database.RunViewModel

class HistoryActivity : AppCompatActivity(), RunHistoryAdapter.OnRunItemClickListener {
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    private val runViewModel: RunViewModel by viewModels {
        RunModelFactory((application as RunApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        runViewModel.runs.observe(this) {
            updateRecyclerView(it)
        }
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setAppAppearance()
        val title = findViewById<TextView>(R.id.text)
        title.text = "History"
    }

    private fun getRunHistoryItems(runs: List<Run>): ArrayList<RunHistoryItem> {
        val runHistoryItems = ArrayList<RunHistoryItem>()

        runs.forEach {
            runHistoryItems.add(RunHistoryItem(it.id, it.date, it.distance, it.duration))
        }

        return runHistoryItems
    }

    private fun updateRecyclerView(runs: List<Run>) {
        recyclerViewHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistory.adapter = RunHistoryAdapter(getRunHistoryItems(runs), this)
    }

    override fun onClick(position: Int, runID: Int) {
        Intent(this, SummaryActivity::class.java).apply {
            putExtra("runID", runID)
        }.also {
            startActivity(it)
        }
    }

    private fun setAppAppearance() {
        val header = findViewById<LinearLayout>(R.id.linearLayout)
        val background = findViewById<ConstraintLayout>(R.id.main)
        if (sharedPreferences.getBoolean("darkMode", false)) {
            header.setBackgroundColor(Color.BLACK)
            background.setBackgroundColor(Color.rgb(170, 170, 170))
        } else {
            background.setBackgroundColor(Color.rgb(255, 255, 255))
            header.setBackgroundColor(sharedPreferences.getInt("color", Color.BLACK))
        }
    }
}