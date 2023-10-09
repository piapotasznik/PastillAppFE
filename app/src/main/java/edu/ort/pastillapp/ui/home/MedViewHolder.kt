package edu.ort.pastillapp.ui.home

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Medicine


class MedViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    private var onMedicineClickListener: OnClickNavigate? = null

    private val medName : TextView = view.findViewById(R.id.medName2)
    private val medDosage : TextView = view.findViewById(R.id.medDosage2)
    val updateBtn2: ImageView = view.findViewById(R.id.updateBtn2)
     fun render(medication: Medicine){

         medName.text = medication.name
         medDosage.text = medication.dosage
//         updateBtn.setOnClickListener {
//             Log.e("click en boton","estoy clickeando!")
//             onMedicineClickListener?.OnClickNavigate()
//         }




    }
    fun setOnMedicineClickListener (listener: OnClickNavigate) {
        onMedicineClickListener = listener
    }
}