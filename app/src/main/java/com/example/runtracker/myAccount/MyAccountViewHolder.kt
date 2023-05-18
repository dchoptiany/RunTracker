package com.example.runtracker.myAccount

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R
import java.text.SimpleDateFormat
import java.util.*


class MyAccountViewHolder(var view : View, var onBlockClickListener: MyAccountAdapter.OnMyAccountClickListener) :  RecyclerView.ViewHolder(view), View.OnClickListener {
    var  sharedPreferences : SharedPreferences
    private var editor: SharedPreferences.Editor
    private val spinnerData = listOf("K", "M")
    var spinner : Spinner
    private val inputText = EditText(view.context)


    var title = view.findViewById<TextView>(R.id.title)
    var data = view.findViewById<TextView>(R.id.data)


    private val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, spinnerData)



    init {
        itemView.setOnClickListener(this)
        inputText.gravity = Gravity.CENTER

        sharedPreferences = view.context.getSharedPreferences("my_account", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        spinner = Spinner(view.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setAdapter(adapter)
        }


    }

    override fun onClick(v: View?) {
        if(adapterPosition==0){

            val alertText = createInputAlert("Name","Enter your name")
            alertText.show()
        }
        if(adapterPosition==1){

            val alertText = createInputAlert("Surname","Enter your surname")
            alertText.show()
        }
        if(adapterPosition==2) {
            val spinnerData = listOf("K", "M")
            val spinner = Spinner(view.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, spinnerData)
            }
            val alertDialog = AlertDialog.Builder(view.context).apply {
                setTitle("Select option")
                setView(spinner)
                setPositiveButton("OK") { dialog, which ->
                    val selectedOption = spinner.selectedItem as String
                    editor.putString("Sex",selectedOption)
                    editor.apply()
                    onBlockClickListener.onBlockClick(adapterPosition)
                }
                setNegativeButton("Anuluj", null)
            }.create()
            alertDialog.show()
        }
        if(adapterPosition==3) {
            val calendarView = CalendarView(view.context)
            val alertDialog = AlertDialog.Builder(view.context)
                .setView(calendarView)
                .setPositiveButton("OK") { dialog, which ->
                    val selectedDate = Date(calendarView.date)
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate)
                    editor.putString("Date of birth",formattedDate)
                    editor.apply()
                    onBlockClickListener.onBlockClick(adapterPosition)
                }
                .setNegativeButton("Cancel", null)
                .create()
            alertDialog.show()
        }

        if(adapterPosition==4){
            val alertText = createInputAlert("Weigth","Enter your weigth")
            alertText.show()
        }

        if(adapterPosition==5){
            val alertText = createInputAlert("Height","Enter your height")
            alertText.show()
        }
    }

    private fun createInputAlert(input : String, title : String) : AlertDialog{
        val parent = inputText.parent as? ViewGroup
        parent?.removeView(inputText)

        val alertText = AlertDialog.Builder(view.context)
            .setTitle(title)
            .setView(inputText)
            .setPositiveButton("OK") { dialog, which ->

                val text = inputText.text.toString()
                editor.putString(input, text)
                editor.apply()
                onBlockClickListener.onBlockClick(adapterPosition)
            }
            .setNegativeButton("Cancel") { dialog, which -> }
            .create()

        return alertText
    }
}
