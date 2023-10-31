package edu.ort.pastillapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.CalendarFragmentDirections
import edu.ort.pastillapp.Models.Dia
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.R

class DateCalendarAdapter (private var dates:MutableList<String>  = mutableListOf(), private val navController: NavController) : RecyclerView.Adapter<DateCalendarViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateCalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return DateCalendarViewHolder(view)
    }

    override fun getItemCount(): Int  = dates.size

    override fun onBindViewHolder(holder: DateCalendarViewHolder, position: Int) {
        holder.render(dates[position])

        holder.seeAll.setOnClickListener {
             val action = CalendarFragmentDirections.actionNavigationCalendarToLogsCalendarFragment(dates[position])
             navController.navigate(action)
        }
    }
    fun updateData(nuevaLista: List<String>) {
        dates.clear()
        dates.addAll(nuevaLista)
        notifyDataSetChanged()
    }

}