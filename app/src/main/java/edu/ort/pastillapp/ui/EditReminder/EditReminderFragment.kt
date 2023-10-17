package edu.ort.pastillapp.ui.EditReminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
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
import androidx.navigation.fragment.findNavController
import edu.ort.pastillapp.R
import edu.ort.pastillapp.databinding.FragmentEditReminderBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.models.Reminder
import java.util.Calendar
import java.util.Locale


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


        reminder?.let {
            viewModel.setReminder(it)
            setReminderValues(it)
        }
//

        if (reminder != null) {
            setReminderValues(reminder)

        }

        val btnCxl = binding.cxlBtn
        btnCxl.setOnClickListener {
            findNavController().popBackStack()
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

//        minMaxvalues()
        dateText.setOnClickListener{
            showDateTimePicker()
        }


    }

    fun showDateTimePicker() {

        val currentLocale = Locale("es")
        Locale.setDefault(currentLocale)

        val config = Configuration()
        config.setLocale(currentLocale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(requireContext(), R.style.TimePicker, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(requireContext(), R.style.TimePicker    ,TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val formattedDate = String.format("%02d-%02d-%04d %02d:%02d", dayOfMonth, month + 1, year, hourOfDay, minute)
                binding.editDateIntake.text = formattedDate
            }, hour, minute, true)



            timePickerDialog.show()
        }, year, month, day)



        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }




    // coloca en los inptus los valores que ya contiene el reminder
    fun setReminderValues(reminder : Reminder){
        val presentationValues = resources.getStringArray(R.array.med_presentation)
        val positionToSelectPresentation = presentationValues.indexOf(reminder.presentation)

        val frecuencyIntervalValues = resources.getStringArray(R.array.frecuency_intake_type)
        val positionToSelectFrInterval = frecuencyIntervalValues.indexOf(reminder.frequencyInterval)

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

        binding.editDateIntake.setText(Helpers().dateToString(reminder.dateTimeStart))
    }


    fun getReminderValues(){
        var editedReminder = viewModel.getReminder() // Supongo que hay un método para obtener el reminder en tu ViewModel

        editedReminder?.let {
            it.medicine = binding.editNameMed.text.toString()
            it.quantity=binding.editDosis.text.toString()
            it.emergencyAlert= binding.emergencyCheckBox.isChecked
            it.presentation=binding.presentationSpinner.toString()
            it.frequencyInterval =binding.editFrecuencyInterval.toString()
            it.frequencyInt=binding.editFrecuencyInt.selectedItem.toString().toInt()

          it.durationType=  binding.editDurationIntake.selectedItem.toString()
            it.durationInt =binding.editDurationNum.selectedItem.toString().toInt()
            it.observation= binding.editNotes.text.toString()

            it.dateTimeStart= Helpers().stringToDate(binding.editDateIntake.text.toString())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // viewModel.reminder.removeObservers(viewLifecycleOwner)
    }


}