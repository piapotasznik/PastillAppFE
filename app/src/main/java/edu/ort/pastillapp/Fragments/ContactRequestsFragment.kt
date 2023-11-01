package edu.ort.pastillapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import edu.ort.pastillapp.Adapters.ContactRequestAdapter
import edu.ort.pastillapp.Models.EmergencyRequestData
import edu.ort.pastillapp.ViewModels.ContactRequestsViewModel

class ContactRequestsFragment : Fragment() {
    private lateinit var v: View

    private lateinit var adapter: ContactRequestAdapter

    private val viewModel: ContactRequestsViewModel by viewModels()
    private var list: MutableList<EmergencyRequestData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_contact_requests, container, false)

//        // Handler para ejecutar la actualización periódica
//        val handler = Handler()
//        val delay: Long = 2000
//        //60000 // Actualiza cada 60 seg. 1000 para pruebas
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                updateEmergencyRequestList()
//                handler.postDelayed(this, delay)
//            }
//        }, delay)
        viewModel.updateEmergencyRequestList()
        return v
    }

    override fun onStart() {
        super.onStart()
        viewModel.emergencyContacts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.updateData(it)
            }
        })
        val backButton = v.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        initRecycleView()
    }

    private fun initRecycleView(){
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactRequestAdapter(list)
        recyclerView.adapter = adapter
    }
}
