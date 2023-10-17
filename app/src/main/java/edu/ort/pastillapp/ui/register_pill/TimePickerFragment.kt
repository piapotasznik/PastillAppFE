package edu.ort.pastillapp.ui.register_pill

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import edu.ort.pastillapp.R
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