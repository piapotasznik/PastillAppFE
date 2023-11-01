package edu.ort.pastillapp.Fragments

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.Adapters.DateCalendarAdapter
import edu.ort.pastillapp.R
import edu.ort.pastillapp.ViewModels.CalendarViewModel
import edu.ort.pastillapp.databinding.FragmentCalendarBinding
import java.util.Calendar
import java.util.Locale


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var reminderAdapter: DateCalendarAdapter
    private val calendarViewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reminderAdapter = DateCalendarAdapter(mutableListOf(), findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        calendarViewModel.remindersList.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                reminderAdapter.updateData(it)
            }
        })
        initRecycleView()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val dateTextFrom = binding.searchFrom
        val dateTextUpTo = binding.searchUpto

        dateTextFrom.setOnClickListener {
            showDatePicker(dateTextFrom)
        }

        dateTextUpTo.setOnClickListener {
            showDatePicker(dateTextUpTo)
        }
        val btnCleanInputs = binding.btnClean
        btnCleanInputs.setOnClickListener {
            dateTextFrom.text = ""
            dateTextUpTo.text = ""
        }

        val btnSearch = binding.btnSearchCalendar
        btnSearch.setOnClickListener {
            if (dateTextFrom.text.isNullOrBlank() || dateTextFrom.text.isEmpty() || dateTextUpTo.text.isNullOrBlank() || dateTextUpTo.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Debe seleccionar ambas fechas",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dateList =
                    generateDateRange(dateTextFrom.text.toString(), dateTextUpTo.text.toString())
                calendarViewModel.remindersList.postValue(dateList)
            }
        }
    }

    private fun showDatePicker(textView: TextView) {
        val currentLocale = Locale("es")
        Locale.setDefault(currentLocale)
        val config = Configuration()
        config.setLocale(currentLocale)
        resources.updateConfiguration(config, resources.displayMetrics)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.TimePicker,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val formattedDate = String.format(
                    "%02d-%02d-%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )
                textView.text = formattedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = 0
        datePickerDialog.show()
    }


    private fun initRecycleView() {
        val recycleView = binding.rvCalendarSearch

        recycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = reminderAdapter
    }


    private fun generateDateRange(startDate: String, endDate: String): List<String> {
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        startCalendar.time = dateFormat.parse(startDate)!!
        endCalendar.time = dateFormat.parse(endDate)!!

        val dateList = mutableListOf<String>()

        while (startCalendar.before(endCalendar)) {
            val date = startCalendar.time
            val formattedDate = dateFormat.format(date)
            dateList.add(formattedDate)
            startCalendar.add(Calendar.DATE, 1)
        }

        dateList.add(endDate)
        return dateList
    }

}