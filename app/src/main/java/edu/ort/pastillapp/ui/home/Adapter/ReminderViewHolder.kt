package edu.ort.pastillapp.ui.home.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Reminder

class ReminderViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private var onMedicineClickListener: OnClickNavigate? = null
    private val medName : TextView = view.findViewById(R.id.medName)
    private val medDosage : TextView = view.findViewById(R.id.medDosage)
    val updateBtn: ImageView = view.findViewById(R.id.updateBtn)
    val archiveBtn: ImageView = view.findViewById(R.id.archivedBtn)
    fun render(reminder: Reminder){
        medName.text = reminder.presentation
        medDosage.text = reminder.quantity.toString()


    }

    fun setOnMedicineClickListener (listener: OnClickNavigate) {
        onMedicineClickListener = listener
    }
}