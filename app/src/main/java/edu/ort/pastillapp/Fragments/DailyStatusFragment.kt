package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.ViewModels.CalendarViewModel
import edu.ort.pastillapp.databinding.FragmentDailyStatusBinding


class DailyStatusFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentDailyStatusBinding? = null
    private val calendarViewModel: CalendarViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment

            _binding = FragmentDailyStatusBinding.inflate(inflater, container, false)
            val root: View = binding.root


            setDailySatusValues()

            return root
        }

    override fun onStart() {
        super.onStart()

        binding.dailyBtnBack.setOnClickListener{
            Log.d("volver btn", "click")
            findNavController().popBackStack()
        }

        calendarViewModel.dailyStatus.observe(viewLifecycleOwner, Observer {

            var dailyStatus = calendarViewModel.dailyStatus.value
            Log.d("arriba 3", " este es ${dailyStatus.toString()}")
            if (dailyStatus != null) {
                binding.dateDailyStatus.text = Helpers().convertDateSM(dailyStatus.date)
                binding.tvSymptomsContent.text = dailyStatus.symptoms.trim('[', ']')
                binding.tvObservationContent.text = dailyStatus.observation
            }
        })

    }


    fun setDailySatusValues(){
       calendarViewModel.getDailyStatus(DailyStatusFragmentArgs.fromBundle(requireArguments()).date)



    }

}