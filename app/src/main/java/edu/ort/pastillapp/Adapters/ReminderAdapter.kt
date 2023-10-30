package edu.ort.pastillapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.HistoricalReminderFragment
import edu.ort.pastillapp.Fragments.HistoricalReminderFragmentDirections
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.Reminder


class ReminderAdapter(
    private var reminders: MutableList<Reminder> = mutableListOf(),
    private val navController: NavController,
    private val fragment: HistoricalReminderFragment
) : RecyclerView.Adapter<ReminderViewHolder>() {
    fun filter(query: String?) {
        val filteredList = mutableListOf<Reminder>()
        for (item in reminders) {
            if (item.medicineName?.lowercase()!!.contains(query!!.lowercase())) {
                filteredList.add(item)
            }
        }
        // reminders = filteredList
        updateData(filteredList)
        notifyDataSetChanged()
    }

    fun onlyActives() {
        val filteredList = mutableListOf<Reminder>()
        for (item in reminders) {
            if (!item.endDateTime?.let { Helpers().dateHasAlreadyPassed(it) }!!) {
                filteredList.add(item)
            }
        }
        // reminders = filteredList
        updateData(filteredList)
        notifyDataSetChanged()
    }

    fun updateData(newData: List<Reminder>) {
        reminders.clear() // Limpia la lista actual
        reminders.addAll(newData) // Agrega los nuevos datos
        Log.d("que pasa", "llegan los reminders al adapter ${newData.toString()}")

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historical_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun getItemCount(): Int = reminders.size

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.render(reminders[position])

        holder.updateBtn.setOnClickListener {
            val action =
                HistoricalReminderFragmentDirections.actionHistoricalReminderFragmentToEditReminderFragment(
                    reminders[position].reminderId
                )
            navController.navigate(action)
        }
        holder.deleteBtn.setOnClickListener {
            holder.showDeleteConfirmationDialog(position, fragment)
        }
    }
}