package edu.ort.pastillapp.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.models.EmergencyRequestData

class contactRequestAdapter (private val emergencyRequests: List<EmergencyRequestData>) :
    RecyclerView.Adapter<contactRequestAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val acceptButton: Button = itemView.findViewById(R.id.buttonAccept)
        val rejectButton: Button = itemView.findViewById(R.id.buttonReject)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emergency_contact_request, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = emergencyRequests[position]

        // Configura el contenido del elemento de la lista
        holder.nameTextView.text = request.name

        // Define las acciones para los botones de aceptar y rechazar aquí
        holder.acceptButton.setOnClickListener {
            // Lógica para aceptar
        }

        holder.rejectButton.setOnClickListener {
            // Lógica para rechazar
        }
    }
    override fun getItemCount(): Int {
        return emergencyRequests.size
    }
}