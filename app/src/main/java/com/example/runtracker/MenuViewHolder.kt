package com.example.runtracker

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MenuViewHolder(val view : View,var onBlockListener : MenuAdapter.OnBlockClickListener) : RecyclerView.ViewHolder(view),View.OnClickListener {

    var text  = view.findViewById<TextView>(R.id.text)
    var image = view.findViewById<ImageView>(R.id.image)

    init{
        view.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        Toast.makeText(view.context,text.text,Toast.LENGTH_SHORT).show()

        // temporary test for Map Fragment
        if(text.text.equals("ACTIVITIES")) {
            var mapIntent = Intent(view.context, MapActivity::class.java)
            view.context.startActivity(mapIntent)
        }
    }
}