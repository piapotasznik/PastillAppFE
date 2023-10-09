package edu.ort.pastillapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextClock
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EditReminderFragment : Fragment() {
lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        v = inflater.inflate(R.layout.fragment_edit_reminder, container, false)

        return v
    }


    override fun onStart() {
        super.onStart()
        val dateText =v.findViewById<EditText>(R.id.editDateIntake)

        minMaxvalues()
        dateText.setOnClickListener{
            showDatePicker()
        }


    }
    fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // Aquí puedes hacer algo con la fecha seleccionada
            // Por ejemplo, actualizar un campo de texto
            // dateTextView.text = "$year-$month-$dayOfMonth"
        }, year, month, day)

        datePickerDialog.show()
    }



    fun minMaxvalues(){
        val hourPicker = v.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = v.findViewById<NumberPicker>(R.id.minutePicker)

// Configura el rango de horas (por ejemplo, de 0 a 23)
        hourPicker.minValue = 0
        hourPicker.maxValue = 23


// Configura el rango de minutos (por ejemplo, de 0 a 59)
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
    }

}