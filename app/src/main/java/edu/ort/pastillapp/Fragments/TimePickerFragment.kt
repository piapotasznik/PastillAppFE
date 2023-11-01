package edu.ort.pastillapp.Fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

class TimePickerFragment : DialogFragment() {

    private var hour: Int
    private var minute: Int
    private var timeObserver: TimePickerDialog.OnTimeSetListener? = null


    init {
        val now = LocalTime.now()
        hour = now.hour
        minute = now.minute
    }

    companion object {
        fun newInstance(observer: TimePickerDialog.OnTimeSetListener): TimePickerFragment {

            return TimePickerFragment().apply {
                timeObserver = observer
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(requireActivity(), timeObserver, hour, minute, false)
    }
}