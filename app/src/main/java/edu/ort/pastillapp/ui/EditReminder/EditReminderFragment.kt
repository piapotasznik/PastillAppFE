package edu.ort.pastillapp.ui.EditReminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.ort.pastillapp.R
import edu.ort.pastillapp.databinding.FragmentEditReminderBinding
import edu.ort.pastillapp.models.Reminder
import java.util.Calendar


class EditReminderFragment : Fragment() {
    private lateinit var viewModel: EditReminderViewModel
    private lateinit var binding: FragmentEditReminderBinding // Referencia al ViewBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentEditReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EditReminderViewModel::class.java)
       val reminder = EditReminderFragmentArgs.fromBundle(requireArguments()).reminder

        Log.e("reminder","el reminder llego OK? ${reminder?.medicine}")
        reminder?.let {
            viewModel.setReminder(it)
        }
//

        if (reminder != null) {
            val presentationValues = resources.getStringArray(R.array.med_presentation)
            val positionToSelectPresentation = presentationValues.indexOf(reminder.presentation)

            val frecuencyIntervalValues = resources.getStringArray(R.array.frecuency_intake_type)
            val positionToSelectFrInterval = frecuencyIntervalValues.indexOf(reminder.frecuencyInterval)

            val durationIntakeValues = resources.getStringArray(R.array.duration_intake)
            val positionToSelectDuration = durationIntakeValues.indexOf(reminder.durationType)

            binding.editNameMed.setText(reminder.medicine)
            binding.editDosis.setText(reminder.quantity)
            binding.emergencyCheckBox.isChecked=reminder.emergencyAlert
            binding.presentationSpinner.setSelection(positionToSelectPresentation)
            binding.editFrecuencyInterval.setSelection(positionToSelectFrInterval)
            binding.editFrecuencyInt.setSelection(reminder.frequencyInt-1)
            binding.editDurationIntake.setSelection(positionToSelectDuration)
            binding.editDurationNum.setSelection(reminder.durationInt-1)
            binding.editNotes.setText(reminder.observation)
        }

//        viewModel.reminder.observe(viewLifecycleOwner, Observer { reminder ->
//            // Actualizar el objeto Reminder cuando haya cambios en los campos
//            reminder?.let {
//                viewModel.updateMedicine(binding.editNameMed.text.toString())
//                // Repite este proceso para los otros campos
//            }
//        })
    }
    override fun onStart() {
        super.onStart()
        val dateText =binding.editDateIntake

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

            val formattedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            binding.editDateIntake.text = formattedDate
        }, year, month, day)

        datePickerDialog.show()
    }



    fun minMaxvalues(){
        val hourPicker = binding.hourPicker
        val minutePicker =binding.minutePicker

// Configura el rango de horas (por ejemplo, de 0 a 23)
        hourPicker.minValue = 0
        hourPicker.maxValue = 23


// Configura el rango de minutos (por ejemplo, de 0 a 59)
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // viewModel.reminder.removeObservers(viewLifecycleOwner)
    }

}