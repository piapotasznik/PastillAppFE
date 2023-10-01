package edu.ort.pastillapp.ui.home

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import java.util.Date


class DateViewHolder(view:View) : RecyclerView.ViewHolder(view) {
    private val tvDay : TextView = view.findViewById(R.id.tvDay)
    private val tvDayNumber : TextView = view.findViewById(R.id.tvDayNumber)
     fun render(date:Dia){

         tvDayNumber.text = date.number.toString()
         if (date.isToday){
             itemView.setBackgroundResource(R.drawable.oval_background)
             tvDay.text = "Hoy"
         } else {
             tvDay.text = date.day
         }


    }
}