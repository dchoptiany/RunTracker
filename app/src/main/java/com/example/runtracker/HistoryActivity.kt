package com.example.runtracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity(), RunHistoryAdapter.OnRunItemClickListener {
    private lateinit var recyclerViewHistory: RecyclerView

    private val runViewModel: RunViewModel by viewModels {
        RunModelFactory((application as RunApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        updateRecyclerView()
    }

    private fun getRunHistoryItems(): ArrayList<RunHistoryItem> {
        val runHistoryItems = ArrayList<RunHistoryItem>()

        if(runViewModel.runs.value != null)
        {
            for(run in runViewModel.runs.value!!) {
                runHistoryItems.add(RunHistoryItem(run.id, run.date, run.distance, run.duration))
            }
        }

        return runHistoryItems
    }

    private fun updateRecyclerView() {
        recyclerViewHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistory.adapter = RunHistoryAdapter(getRunHistoryItems(), this)
    }

    override fun onClick(position: Int, runID: Int) {
        Intent(this, SummaryActivity::class.java).apply {
            putExtra("runID", runID)
        }.also {
            startActivity(it)
        }
    }
}