package edu.ort.pastillapp.ui.home.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.models.ReminderLogToday
import edu.ort.pastillapp.ui.home.HomeFragmentDirections

class TodayReminderAdapter(private val reminders: List<ReminderLogToday>, private val navController: NavController) : RecyclerView.Adapter<TodayReminderViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayReminderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
            return TodayReminderViewHolder(view)
        }

        override fun getItemCount(): Int  = reminders.size



        override fun onBindViewHolder(holder: TodayReminderViewHolder, position: Int) {
            holder.render(reminders[position])

            holder.updateBtn.setOnClickListener{
                val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminders[position].reminderId)
                navController.navigate(action)
            }
//
            holder.archiveBtn.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToReminderFragment(reminders[position].reminderId)
                navController.navigate(action)

            }
//        }

    }


}