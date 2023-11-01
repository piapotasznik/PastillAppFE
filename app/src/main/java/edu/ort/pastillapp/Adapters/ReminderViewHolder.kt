package edu.ort.pastillapp.Adapters

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.HistoricalReminderFragment
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Listeners.OnClickNavigate
import edu.ort.pastillapp.Models.Reminder

class ReminderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private var onMedicineClickListener: OnClickNavigate? = null

    private val medName : TextView = view.findViewById(R.id.medNameHistorical)
    private val initialDate : TextView = view.findViewById(R.id.initialDate)
//    private val medPresentation : TextView = view.findViewById(R.id.medPresentationHistorical)

    val updateBtn: ImageView = view.findViewById(R.id.editHistorical)
    val deleteBtn: ImageView = view.findViewById(R.id.deleteHistorical)
    val archiveBtn: ImageView = view.findViewById(R.id.seeHistorical)

    fun render(reminder: Reminder){
        medName.text = reminder.medicineName
        initialDate.text = Helpers().convertOnlyDate(reminder.dateTimeStart.toString())
//        medPresentation.text = reminder.presentation.toString()
        if (!!reminder.endDateTime?.let { Helpers().dateHasAlreadyPassed(it) }!!) {
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
        } else {
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
        }
    }

    fun showDeleteConfirmationDialog(position: Int, fragment: HistoricalReminderFragment) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Confirmar Eliminación")
        builder.setMessage("¿Estás seguro de que deseas eliminar este Recordatorio?")

        builder.setPositiveButton("Eliminar") { _, _ ->
            // Llama a la función de eliminación del Fragment
            fragment.deleteReminder(position)
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun setOnMedicineClickListener(listener: OnClickNavigate) {
        onMedicineClickListener = listener
    }
}