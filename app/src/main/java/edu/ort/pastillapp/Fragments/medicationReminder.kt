package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Models.ReminderLogRepository

class medicationReminder : Fragment() {
    private val reminderLogRepository = ReminderLogRepository(requireContext())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recuperar el ID del ReminderLog desde los argumentos
        val reminderLogId = arguments?.getString("reminderLogId")

        if (reminderLogId != null) {
            // Realiza una solicitud al backend para obtener el ReminderLog utilizando reminderLogId
            reminderLogRepository.obtenerReminderLog(reminderLogId,
                onSuccess = { reminderLog ->
                    // Actualiza las vistas del fragmento con los datos del ReminderLog
                    view?.findViewById<TextView>(R.id.textView3)?.text = reminderLog.name
                    view?.findViewById<TextView>(R.id.textView5)?.text = reminderLog.dosage.toString()
                    view?.findViewById<TextView>(R.id.textView6)?.text = reminderLog.presentation
                    view?.findViewById<TextView>(R.id.textView7)?.text = reminderLog.dateTime

                    // Resto de las actualizaciones de vistas
                },
                onError = { error ->
                    // Manejar errores, por ejemplo, mostrar un mensaje de error
                    Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                }
            )
        }

        return inflater.inflate(R.layout.fragment_medication_reminder, container, false)
    }
}