package edu.ort.pastillapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import java.util.Date

class DateAdapter (private val dates:List<Dia>) : RecyclerView.Adapter<DateViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
    return DateViewHolder(view)
    }

    override fun getItemCount(): Int  = dates.size



    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.render(dates[position])
    }

}