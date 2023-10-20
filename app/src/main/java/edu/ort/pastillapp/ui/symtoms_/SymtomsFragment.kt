package edu.ort.pastillapp.ui.symtoms_

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.R
import edu.ort.pastillapp.databinding.FragmentSymtomsBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Medicine
import edu.ort.pastillapp.models.Reminder
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import edu.ort.pastillapp.ui.home.DateAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SymtomsFragment : Fragment()  {
    private var _binding: FragmentSymtomsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(SymtomsViewModel::class.java)

        _binding = FragmentSymtomsBinding.inflate(inflater, container, false)
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