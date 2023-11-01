package edu.ort.pastillapp.Fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.Adapters.CalendarLogsAdapter
import edu.ort.pastillapp.Adapters.ReminderAdapter
import edu.ort.pastillapp.ViewModels.HistoricalReminderViewModel
import edu.ort.pastillapp.ViewModels.LogsCalendarViewModel
import edu.ort.pastillapp.databinding.FragmentLogsCalendarBinding

class LogsCalendarFragment : Fragment() {

    private lateinit var binding: FragmentLogsCalendarBinding
    private  val viewModel: LogsCalendarViewModel by viewModels()
    private lateinit var adapter: CalendarLogsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =  FragmentLogsCalendarBinding.inflate(inflater, container, false)

        val date = LogsCalendarFragmentArgs.fromBundle(requireArguments()).date
        binding.logsCalendarDate.text = date

        viewModel.getLogs(date)
        adapter = CalendarLogsAdapter(mutableListOf())

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.logs.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                adapter.updateData(it)
            }
        })
        binding.btnLogsCalendarBack.setOnClickListener {
            findNavController().popBackStack()
        }
        initRecycleView()
    }

    private fun initRecycleView(){
        val recycleView = binding.rvLogsCalendar

        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = adapter
    }
}