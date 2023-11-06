package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.ort.pastillapp.databinding.FragmentSymptomsBinding
import edu.ort.pastillapp.ViewModels.SymptomsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SymptomsFragment : Fragment()  {
    private var _binding: FragmentSymptomsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(SymptomsViewModel::class.java)

        _binding = FragmentSymptomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDate = Date()


        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


        val formattedDate = dateFormat.format(currentDate)



        val textView: TextView = binding.textViewDateToday

        homeViewModel.text.observe(viewLifecycleOwner) {
            Log.d("SymptomsFragment", "Fecha formateada: $formattedDate")
            textView.text = formattedDate

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}