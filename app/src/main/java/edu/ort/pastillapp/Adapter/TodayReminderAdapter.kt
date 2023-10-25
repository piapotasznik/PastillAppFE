package edu.ort.pastillapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.ReminderLogToday
//import edu.ort.pastillapp.ui.home.HomeFragmentDirections

class TodayReminderAdapter(
    private var reminders: MutableList<ReminderLogToday>,
    private val navController: NavController
) : RecyclerView.Adapter<TodayReminderViewHolder>() {

    // Método para actualizar la lista de recordatorios
    fun actualizarDatos(nuevaLista: List<ReminderLogToday>) {
        reminders.clear()
        reminders.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return TodayReminderViewHolder(view)
    }

    override fun getItemCount(): Int  = reminders.size

    override fun onBindViewHolder(holder: TodayReminderViewHolder, position: Int) {
        holder.render(reminders[position])

        holder.updateBtn.setOnClickListener {
//            val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminders[position].reminderId)
//            navController.navigate(action)
        }

        holder.archiveBtn.setOnClickListener {
//            val action = HomeFragmentDirections.actionNavigationHomeToReminderFragment(reminders[position].reminderId)
//            navController.navigate(action)
        }
    }
}
