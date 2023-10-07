package edu.ort.pastillapp.ui.symtoms_

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.models.Reminder

class ReminderViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val reminderName : TextView = view.findViewById(R.id.reminderName)
    private val reminderDosage : TextView = view.findViewById(R.id.reminderDosage)
    fun render(reminder: Reminder){

        reminderName.text = reminder.medicine
        reminderDosage.text = reminder.quantity



    }
}