package edu.ort.pastillapp.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.models.Reminder

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
            Log.e("reminderAdapter", "el reminder que se esta pasando es  ${reminders[position].medicine}")
            navController.navigate(action)
        }
    }

}