package edu.ort.pastillapp.Adapters

import android.opengl.Visibility
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.Listeners.OnClickNavigate
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.R

class CalendarLogsViewHolder (val view: View) : RecyclerView.ViewHolder(view) {


    private val time: TextView = view.findViewById(R.id.timeLogsCalendar)
    private val medName: TextView = view.findViewById(R.id.medLogsCalendar)
    private val takenTrue: ImageView = view.findViewById(R.id.takenLogsCalendarTrue)
    private val takenFalse: ImageView = view.findViewById(R.id.takenLogsCalendarFalse)
    private val presentation: TextView = view.findViewById(R.id.presentationLogsCalendar)



    fun render(logsFrom: ReminderLogToday) {
        val timeOnly = Helpers().convertirFechaSoloHora(logsFrom.dateTime)

        time.text = timeOnly
        medName.text = logsFrom.name.toString()
        presentation.text = logsFrom.presentation.toString()
        if(logsFrom.taken){
            takenFalse.visibility = View.GONE
            takenTrue.visibility = View.VISIBLE
        }



    }
}
