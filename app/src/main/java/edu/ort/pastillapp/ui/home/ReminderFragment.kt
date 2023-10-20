package edu.ort.pastillapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import edu.ort.pastillapp.databinding.FragmentReminderBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.models.Reminder


class ReminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reminder = ReminderFragmentArgs.fromBundle(requireArguments()).reminder

        Log.e("reminder llego",reminder.toString())

        if (reminder != null) {
             setReminderValues(reminder)

        }

        val btnCxl = binding.cxlBtn2
        btnCxl.setOnClickListener {
            findNavController().popBackStack()
        }
    }

  fun setReminderValues(reminder : Reminder){
      reminder.dateTimeStart?.let { Log.e("put33 2", it) }
        binding.nameMed.setText(reminder.medicineId.toString())
        binding.tvDosis.setText(reminder.quantity.toString())
        binding.emergencyCheckBox2.isChecked=reminder.emergencyAlert
        binding.tvPresentation1.setText(reminder.presentation.toString())
        binding.frequencyInterval.setText(Helpers().translateFrequency(reminder.frequencyText.toString()))
        binding.frequencyInt.setText(reminder.frequencyNumber.toString())
        binding.durationIntake.setText(Helpers().translateFrequency(reminder.intakeTimeText.toString()))
        binding.durationNum.setText(reminder.intakeTimeNumber.toString())
        binding.notes.setText(reminder.observation)

        val dateTimeStartFormat = reminder.dateTimeStart?.let { Helpers().convertirFecha(it) }
        binding.dateIntakeStart.setText(dateTimeStartFormat)
 }
}