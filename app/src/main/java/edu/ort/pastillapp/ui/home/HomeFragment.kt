package edu.ort.pastillapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.databinding.FragmentHomeBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.Medicine
import edu.ort.pastillapp.models.Reminder
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import edu.ort.pastillapp.services.PaginateResponse

import edu.ort.pastillapp.ui.symtoms_.SymtomsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() , OnClickNavigate {

    private lateinit var adapter: ReminderAdatper

    private val reminderList = mutableListOf<Reminder>()

    private var _binding: FragmentHomeBinding? = null



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)





        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onStart() {
        super.onStart()

            getAllReminders()

        intiRecylcerView()


    }

    override fun onResume() {
        super.onResume()

        getAllReminders()

        intiRecylcerView()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun intiRecylcerView(){
        val dateAdapter = DateAdapter(Helpers().getDayOfMoth())
        val posicionInicial = Helpers().dayToday()+2

        binding.rvDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = dateAdapter
        binding.rvDate.smoothScrollToPosition(posicionInicial)


        adapter = ReminderAdatper(reminderList, findNavController())

        binding.medList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.medList.adapter = adapter
    }





    override fun OnClickNavigate(reminder :Reminder) {
        val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminder)
        Log.e("click","estoy clickeando aqui!!!")
        this.findNavController().navigate(action)
    }

    fun getAllReminders(){
        val service = ActivityServiceApiBuilder.createReminder()

        service.getReminders().enqueue(object : Callback<List<Reminder>> {
            override fun onResponse(
                call: Call<List<Reminder>>,
                response: Response<List<Reminder>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()
                    Log.e("chau", response.body().toString())
                    Log.e("chau", "estoy dentrod e la respuesta")

                    if (responseReminders != null) {
                        reminderList.clear()
                        reminderList.addAll(responseReminders)

                    }

                    // Actualizar el RecyclerView
                    adapter.notifyDataSetChanged()


                } else {
                    Log.e("chau", "respuesta no exitosa")
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                }

            }

            override fun onFailure(call: Call<List<Reminder>>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })

    }

}