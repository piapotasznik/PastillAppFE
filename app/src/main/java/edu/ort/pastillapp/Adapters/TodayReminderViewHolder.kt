package edu.ort.pastillapp.Adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.Listeners.OnClickNavigate
import edu.ort.pastillapp.Models.ReminderLogToday

class TodayReminderViewHolder (val view: View) : RecyclerView.ViewHolder(view) {

    private var onMedicineClickListener: OnClickNavigate? = null
    private val medName : TextView = view.findViewById(R.id.medName)
    val checkBox : CheckBox = view.findViewById(R.id.medDosage)
    private val nextIntake : TextView = view.findViewById(R.id.nextIntake)
    private val takenImage :ImageView = view.findViewById(R.id.tvPill)
    private val takenImageGreen :ImageView = view.findViewById(R.id.tvPillGreen)

    val chTaken: CheckBox = view.findViewById(R.id.medDosage)
    val updateBtn: ImageView = view.findViewById(R.id.updateBtn)
    val archiveBtn: ImageView = view.findViewById(R.id.archivedBtn)

    fun render(reminder: ReminderLogToday){
        medName.text = "${reminder.name}" // - ${reminder.dosage}
        checkBox.isChecked = reminder.taken
        nextIntake.text = "Horario: ${Helpers().convertDateOnlyTime( reminder.dateTime)}"
        pillTaken(reminder.taken)
        if (reminder.taken){
            checkBox.isEnabled= false
        }


    }

    fun setOnMedicineClickListener(listener: OnClickNavigate) {
        onMedicineClickListener = listener
    }

    fun pillTaken(boolean:Boolean){
        if(boolean){
            takenImage.visibility = View.INVISIBLE
            takenImageGreen.visibility = View.VISIBLE
        }
    }

}