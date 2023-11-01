package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.ort.pastillapp.databinding.FragmentSymptomsBinding
import edu.ort.pastillapp.ViewModels.SymptomsViewModel


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
       // val textView: TextView = binding.textSymtoms
        homeViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}