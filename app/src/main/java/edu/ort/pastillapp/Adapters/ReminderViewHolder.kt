package edu.ort.pastillapp.Adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Listeners.OnClickNavigate
import edu.ort.pastillapp.Models.Reminder

class ReminderViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private var onMedicineClickListener: OnClickNavigate? = null
    private val medName : TextView = view.findViewById(R.id.medNameHistorical)
    private val medPresentation : TextView = view.findViewById(R.id.medPresentationHistorical)
    val updateBtn: ImageView = view.findViewById(R.id.editHistorical)
    val deleteBtn: ImageView = view.findViewById(R.id.deleteHistorical)
    fun render(reminder: Reminder){
        medName.text = reminder.frequencyText
        medPresentation.text = reminder.presentation.toString()
        Log.d("que pasa","estoy en el holder x cada uno ${reminder.toString()}")
    }

    fun setOnMedicineClickListener (listener: OnClickNavigate) {
        onMedicineClickListener = listener
    }
}