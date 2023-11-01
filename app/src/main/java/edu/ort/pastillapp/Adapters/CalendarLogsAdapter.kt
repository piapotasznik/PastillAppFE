package edu.ort.pastillapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.HomeFragmentDirections
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.R

class CalendarLogsAdapter (private var reminders: MutableList<ReminderLogToday>
) : RecyclerView.Adapter<CalendarLogsViewHolder>() {

    // Método para actualizar la lista de recordatorios
    fun updateData(nuevaLista: List<ReminderLogToday>) {
        reminders.clear()
        reminders.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarLogsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_logs, parent, false)
        return CalendarLogsViewHolder(view)
    }

    override fun getItemCount(): Int  = reminders.size

    override fun onBindViewHolder(holder: CalendarLogsViewHolder, position: Int) {
        holder.render(reminders[position])




    }
}