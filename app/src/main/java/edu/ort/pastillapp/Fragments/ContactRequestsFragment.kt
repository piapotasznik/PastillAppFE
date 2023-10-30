package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiContactEmergencyList
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Adapters.ContactRequestAdapter
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Models.EmergencyRequestData

class ContactRequestsFragment : Fragment() {
    lateinit var v: View

    private var email: String = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())
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

        val backButton = v.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ContactRequestAdapter(list)
        recyclerView.adapter = adapter

        // Handler para ejecutar la actualización periódica
        val handler = Handler()
        val delay: Long = 2000
        //60000 // Actualiza cada 60 seg. 1000 para pruebas
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateEmergencyRequestList()
                handler.postDelayed(this, delay)
            }
        }, delay)
        updateEmergencyRequestList()
        return v
    }

    fun updateEmergencyRequestList() {
        val userService = ActivityServiceApiBuilder.create()
        val call = userService.getEmergencyRequests(email)


        call.enqueue(object : Callback<ApiContactEmergencyList> {
            override fun onResponse(
                call: Call<ApiContactEmergencyList>,
                response: Response<ApiContactEmergencyList>
            ) {

                val emergencyRequestResponse = response.body()
                val emergencyRequests =
                    emergencyRequestResponse?.emergencyRequestList?.toMutableList()
                Log.d(
                    "contactRequests",
                    "Tamaño de la lista de solicitudes: ${emergencyRequests?.size ?: 0}"
                )

                if (emergencyRequests != null && emergencyRequests.isNotEmpty()) {
                    list.clear()
                    list.addAll(emergencyRequests)
                } else {
                    val textViewNoRequests = v.findViewById<TextView>(R.id.textViewNoRequests)
                    textViewNoRequests.visibility = View.VISIBLE
                    Log.d("contactRequests", "Llegó a 'No hay solicitudes pendientes'")
                }
            }

            override fun onFailure(call: Call<ApiContactEmergencyList>, t: Throwable) {
                // Maneja errores de comunicación aquí
            }
        })
    }
}
