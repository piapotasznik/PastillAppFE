package edu.ort.pastillapp.ui.symtoms_

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.models.Medicine


class MedViewHolder(view:View) : RecyclerView.ViewHolder(view) {
    private val medName : TextView = view.findViewById(R.id.medName)
    private val medDosage : TextView = view.findViewById(R.id.medDosage)
     fun render(medication: Medicine){

         medName.text = medication.name
         medDosage.text = medication.dosage




    }
}