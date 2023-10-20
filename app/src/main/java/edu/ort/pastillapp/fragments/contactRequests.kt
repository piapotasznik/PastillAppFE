package edu.ort.pastillapp.fragments
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.UserSingleton
import edu.ort.pastillapp.adapters.contactRequestAdapter
import edu.ort.pastillapp.models.EmergencyRequestResponse
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class contactRequests : Fragment() {
    lateinit var v: View

    //CAMBIAR CUANDO ESTE LA PERSISTENC
    private var Correo: String = UserSingleton.currentUserEmail.toString()

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
            findNavController().navigate(R.id.action_contactRequests2_to_navigation_profile)
        }

        val userService = ActivityServiceApiBuilder.create()
        val userMail = Correo
        val call = userService.getEmergencyRequests(userMail)

        call.enqueue(object : Callback<EmergencyRequestResponse> {
            override fun onResponse(call: Call<EmergencyRequestResponse>, response: Response<EmergencyRequestResponse>) {

                val emergencyRequestResponse = response.body()
                val emergencyRequests = emergencyRequestResponse?.emergencyRequestList
                Log.d("contactRequests", "Tamaño de la lista de solicitudes: ${emergencyRequests?.size ?: 0}")

                val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                if (emergencyRequests != null && emergencyRequests.isNotEmpty()) {
                    val adapter = contactRequestAdapter(emergencyRequests)
                    recyclerView.adapter = adapter
                } else {
                    val textViewNoRequests = v.findViewById<TextView>(R.id.textViewNoRequests)
                    textViewNoRequests.visibility = View.VISIBLE
                    Log.d("contactRequests", "Llegó a 'No hay solicitudes pendientes'")
                }
            }
            override fun onFailure(call: Call<EmergencyRequestResponse>, t: Throwable) {
                // Maneja errores de comunicación aquí
            }
        })

        return v
    }
}
