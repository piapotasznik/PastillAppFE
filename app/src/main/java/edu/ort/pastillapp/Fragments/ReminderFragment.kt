package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import edu.ort.pastillapp.databinding.FragmentReminderBinding
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding
    private var reminder: Reminder? = null

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
        val reminderId = ReminderFragmentArgs.fromBundle(requireArguments()).reminderId
        getReminderByID(reminderId)


        val btnCxl = binding.cxlBtn2
        btnCxl.setOnClickListener {
            findNavController().popBackStack()
        }
    }

  fun setReminderValues(reminder : Reminder){
      reminder.dateTimeStart?.let { Log.e("put33 2", it) }
        binding.nameMed.setText(reminder.medicineName.toString())
        binding.tvDosis.setText(reminder.quantity.toString())
        binding.emergencyCheckBox2.isChecked=reminder.emergencyAlert
        binding.tvPresentation1.setText(reminder.presentation.toString())
        binding.frequencyInterval.setText(" ${Helpers().translateFrequency(reminder.frequencyText.toString())}    ${reminder.frequencyNumber.toString()} ")
//        binding.frequencyInt.setText(reminder.frequencyNumber.toString())
        binding.durationIntake.setText(" ${Helpers().translateFrequency(reminder.intakeTimeText.toString())}   ${reminder.intakeTimeNumber.toString()} ")
       // binding.durationNum.setText(reminder.intakeTimeNumber.toString())
        binding.notes.setText(reminder.observation)

        val dateTimeStartFormat = reminder.dateTimeStart?.let { Helpers().convertDate(it) }
        binding.dateIntakeStart.setText(dateTimeStartFormat)
 }

    fun getReminderByID(reminderId:Int){
        Log.e("llamando al reminder","llamado exitoso id :${reminderId}")
        val service = ActivityServiceApiBuilder.createReminder()

        service.getReminderId(reminderId).enqueue(object : Callback<Reminder> {
            override fun onResponse(
                call: Call<Reminder>,
                response: Response<Reminder>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminder = response.body()
                    Log.e("llamando","responde del reminder${responseReminder.toString()}")
                    if (responseReminder != null) {
                        reminder = responseReminder
                        Log.e("llamando","responde no es null ${reminder.toString()}")
                    }
                    reminder?.let {
                        setReminderValues(it)
                    }


                } else {
                    Log.e("llamando", "respuesta no exitosa ${response}")

                }

            }

            override fun onFailure(call: Call<Reminder>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })

    }
}