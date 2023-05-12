package com.example.runtracker

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

import java.util.*


class MyAccountViewHolder(var view : View) :  RecyclerView.ViewHolder(view), View.OnClickListener {
    lateinit var  sharedPreferences : SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    val calendarView = CalendarView(view.context)
    val spinnerData = listOf("K", "M")
    var spinner : Spinner
    val inputText = EditText(view.context)


    // Utworzenie adaptera dla Spinnera
    val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, spinnerData)



    init {
        itemView.setOnClickListener(this)

        sharedPreferences = view.context.getSharedPreferences("my_account", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Utworzenie obiektu Spinner
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
            val alertText = createInputAlert("name")
            alertText.show()
        }
        if(adapterPosition==1){
            val alertText = createInputAlert("surname")
            alertText.show()
        }
        if(adapterPosition==2) {
            val alertDialog = AlertDialog.Builder(view.context).apply {
                setTitle("Select option")
                setView(spinner)
                setPositiveButton("OK") { dialog, which ->

                    val selectedOption = spinner.selectedItem as String
                    editor.putString("sex",selectedOption)
                    editor.apply()

                }
                setNegativeButton("Anuluj", null)
            }.create()
        }
        if(adapterPosition==3) {
            val alertDialog = AlertDialog.Builder(view.context)
                .setView(calendarView)
                .setPositiveButton("OK") { dialog, which ->

                    val selectedDate = Date(calendarView.date)
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate)
                    editor.putString("birthDate",formattedDate)
                }
                .setNegativeButton("Cancel") { dialog, which -> }
                .create()
        }

        if(adapterPosition==4){
            val alertText = createInputAlert("weigh")
            alertText.show()
        }
    }

    private fun createInputAlert(input : String) : AlertDialog{
        val alertText = AlertDialog.Builder(view.context)
            .setTitle("Enter text")
            .setView(inputText)
            .setPositiveButton("OK") { dialog, which ->

                val text = inputText.text.toString()
                editor.putString(input, text)
                editor.apply()
            }
            .setNegativeButton("Cancel") { dialog, which -> }
            .create()

        return alertText
    }
}