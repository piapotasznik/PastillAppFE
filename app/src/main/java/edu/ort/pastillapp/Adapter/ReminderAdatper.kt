package edu.ort.pastillapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.HomeFragmentDirections
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.Reminder

class ReminderAdatper  (private val reminders:List<Reminder>,private val navController: NavController) : RecyclerView.Adapter<ReminderViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun getItemCount(): Int  = reminders.size



    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.render(reminders[position])

        holder.updateBtn.setOnClickListener{
            val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminders[position])
            navController.navigate(action)
        }

        holder.archiveBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToReminderFragment(reminders[position])
            navController.navigate(action)
        }
    }

}