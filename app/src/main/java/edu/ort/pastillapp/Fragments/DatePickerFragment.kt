package edu.ort.pastillapp.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment: DialogFragment() {

    private var dateObserver: DatePickerDialog.OnDateSetListener? = null

    companion object {
        fun newInstance(observer: DatePickerDialog.OnDateSetListener): DatePickerFragment {

            return DatePickerFragment().apply {
                dateObserver = observer
            }
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), dateObserver, year, month, day)
    }
    fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
    }
}
