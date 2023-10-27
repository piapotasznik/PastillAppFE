package edu.ort.pastillapp.Fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.Adapters.ReminderAdatper
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.R
import edu.ort.pastillapp.ViewModels.HistoricalReminderViewModel
import edu.ort.pastillapp.databinding.FragmentHistoricalReminderBinding
import edu.ort.pastillapp.databinding.FragmentHomeBinding

class HistoricalReminderFragment : Fragment() {



    private  val viewModel: HistoricalReminderViewModel by viewModels()
    private lateinit var reminderAdapter: ReminderAdatper
    private val binding get() = _binding!!
    private var _binding: FragmentHistoricalReminderBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reminderAdapter = ReminderAdatper(mutableListOf(), findNavController())

        viewModel.onCreate()




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoricalReminderBinding.inflate(inflater, container, false)
        val root: View = binding.root




        viewModel.remindersList.observe(viewLifecycleOwner, Observer {

            reminderAdapter.updateData(it)
        })
        initRecycleView()

        return root
    }

    fun initRecycleView(){
        val recycleView = binding.rvHistoricalReminder

        recycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = reminderAdapter

    }

}