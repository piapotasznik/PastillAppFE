package edu.ort.pastillapp.Adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.Models.ApiEmergencyContactResponseDTO
import edu.ort.pastillapp.Models.EmergencyRequestData
import edu.ort.pastillapp.R
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactRequestAdapter(private val emergencyRequests: MutableList<EmergencyRequestData>) :
    RecyclerView.Adapter<ContactRequestAdapter.ViewHolder>() {

    private var email: String =
        SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val acceptButton: Button = itemView.findViewById(R.id.buttonAccept)
        val rejectButton: Button = itemView.findViewById(R.id.buttonReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emergency_contact_request, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emergencyRequests.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = emergencyRequests[position]

        // Configura el contenido del elemento de la lista
        holder.nameTextView.text = request.name

        // Define las acciones para los botones de aceptar y rechazar aquí
        holder.acceptButton.setOnClickListener {
            if (request != null) {
                sendResponseToContact(request.emergencyRequestId, true, email)
                removeRequest(position)
            }
        }

        holder.rejectButton.setOnClickListener {
            if (request != null) {
                sendResponseToContact(request.emergencyRequestId, false, email)
                removeRequest(position)
            }
        }
    }

    private fun removeRequest(position: Int) {
        if (position in 0 until emergencyRequests.size) {
            emergencyRequests.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    private fun sendResponseToContact(
        emergencyRequestId: Int,
        response: Boolean,
        userMail: String
    ) {
        val userService = ActivityServiceApiBuilder.create()

        if (emergencyRequestId != null) {
            val emergencyContactResponse =
                ApiEmergencyContactResponseDTO(userMail, emergencyRequestId, response)
            val call = userService.sendEmergencyRequestResponse(emergencyContactResponse)

            call.enqueue(object : Callback<ApiContactEmergencyServerResponse> {
                override fun onResponse(
                    call: Call<ApiContactEmergencyServerResponse>,
                    response: Response<ApiContactEmergencyServerResponse>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null && apiResponse.isSuccess) {
                            // Procesar la respuesta exitosa del servidor

                        }
                    } else {
                        // Procesar una respuesta de error
                    }
                }

                override fun onFailure(
                    call: Call<ApiContactEmergencyServerResponse>,
                    t: Throwable
                ) {
                    // Manejar errores en la comunicación con el servidor
                }
            })
        }

    }

}