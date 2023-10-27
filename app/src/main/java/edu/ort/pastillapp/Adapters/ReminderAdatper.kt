package edu.ort.pastillapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.HistoricalReminderFragmentDirections
import edu.ort.pastillapp.Fragments.HomeFragmentDirections
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.Reminder

class ReminderAdatper  (private val reminders:MutableList<Reminder>  = mutableListOf(),private val navController: NavController) : RecyclerView.Adapter<ReminderViewHolder>(){


    fun updateData(newData: List<Reminder>) {
        reminders.clear() // Limpia la lista actual
        reminders.addAll(newData) // Agrega los nuevos datos
        Log.d("que pasa", "llegan los reminders al adapter ${newData.toString()}")

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historical_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun getItemCount(): Int  = reminders.size

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.render(reminders[position])

        holder.updateBtn.setOnClickListener{
            val action = HistoricalReminderFragmentDirections.actionHistoricalReminderFragmentToEditReminderFragment(reminders[position].reminderId)
            navController.navigate(action)
        }

//        holder.deleteBtn.setOnClickListener {
//            val action = HomeFragmentDirections.actionNavigationHomeToReminderFragment(reminders[position].reminderId)
//            navController.navigate(action)
//        }
    }

}