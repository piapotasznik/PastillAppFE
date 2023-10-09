package edu.ort.pastillapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.R
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Medicine

class MedAdapter (private val medication:List<Medicine>,private val navController: NavController): RecyclerView.Adapter<MedViewHolder>(){

   private var onMedicineClickListener: OnClickNavigate? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medication, parent, false)
    return MedViewHolder(view)
    }

    override fun getItemCount(): Int  = medication.size



    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        holder.render(medication[position])

//        holder.updateBtn.setOnClickListener{
//            val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment()
//            navController.navigate(action)
//        }
    }
    fun setOnMedicineClickListener (listener: OnClickNavigate) {
        onMedicineClickListener = listener
    }


}