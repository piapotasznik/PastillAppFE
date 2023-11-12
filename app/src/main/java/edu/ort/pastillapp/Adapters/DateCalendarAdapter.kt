package edu.ort.pastillapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.ort.pastillapp.Fragments.CalendarFragmentDirections
import edu.ort.pastillapp.Listeners.OnItemClickListener

import edu.ort.pastillapp.R

class DateCalendarAdapter (private var dates:MutableList<String>  = mutableListOf(), private val navController: NavController) : RecyclerView.Adapter<DateCalendarViewHolder>(){

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateCalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return DateCalendarViewHolder(view)
    }

    override fun getItemCount(): Int  = dates.size

    override fun onBindViewHolder(holder: DateCalendarViewHolder, position: Int) {
        holder.render(dates[position])

        holder.seeAll.setOnClickListener {
//             val action = CalendarFragmentDirections.actionNavigationCalendarToLogsCalendarFragment(dates[position])
//             navController.navigate(action)
            onItemClickListener?.onItemClick(dates[position],0)
        }
        holder.dailyStatusBtn.setOnClickListener {
//             val action = CalendarFragmentDirections.actionNavigationCalendarToLogsCalendarFragment(dates[position])
//             navController.navigate(action)
            onItemClickListener?.onItemClick(dates[position],1)
        }
    }
    fun updateData(nuevaLista: List<String>) {
        dates.clear()
        dates.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }


}