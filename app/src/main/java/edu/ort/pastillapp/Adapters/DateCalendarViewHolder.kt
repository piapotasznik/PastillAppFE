package edu.ort.pastillapp.Adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Models.Day
import edu.ort.pastillapp.R

class DateCalendarViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val tvDay : TextView = view.findViewById(R.id.dateCalendar)
    val seeAll :TextView = view.findViewById(R.id.detailsCalendar)

    fun render(date: String){
        tvDay.text = date


    }
}