package edu.ort.pastillapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.HomeFragmentDirections
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.ReminderLogToday

class TodayReminderAdapter(
    private var reminders: MutableList<ReminderLogToday>,
    private val navController: NavController
) : RecyclerView.Adapter<TodayReminderViewHolder>() {

    // Método para actualizar la lista de recordatorios
    fun updateData(newList: List<ReminderLogToday>) {
        reminders.clear()
        reminders.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayReminderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return TodayReminderViewHolder(view)
    }

    override fun getItemCount(): Int = reminders.size

    override fun onBindViewHolder(holder: TodayReminderViewHolder, position: Int) {
        holder.render(reminders[position])

        holder.updateBtn.setOnClickListener {
            val action =
                HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminders[position].reminderId)
            navController.navigate(action)
        }

        holder.archiveBtn.setOnClickListener {
            val action =
                HomeFragmentDirections.actionNavigationHomeToReminderFragment(reminders[position].reminderId)
            navController.navigate(action)
        }
    }
}
