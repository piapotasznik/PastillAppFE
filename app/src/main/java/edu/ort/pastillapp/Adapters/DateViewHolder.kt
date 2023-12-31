package edu.ort.pastillapp.Adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.Day


class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvDay: TextView = view.findViewById(R.id.tvDay)
    private val tvDayNumber: TextView = view.findViewById(R.id.tvDayNumber)
    fun render(date: Day) {
        tvDayNumber.text = date.number.toString()
        if (date.isToday) {
            itemView.setBackgroundResource(R.drawable.oval_background)
            tvDay.text = "Hoy"
        } else {
            tvDay.text = date.day
        }
    }
}