package com.example.runtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyAccountAdapter(val menuItem : ArrayList<MyAccountItem>) : RecyclerView.Adapter<MyAccountViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_account_item,parent,false)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            parent.height /7
        )
        view.layoutParams = layoutParams
        return MyAccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuItem.size
    }

    override fun onBindViewHolder(holder: MyAccountViewHolder, position: Int) {

    }


}