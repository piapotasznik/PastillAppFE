package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.Adapters.ReminderAdapter
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.ViewModels.HistoricalReminderViewModel
import edu.ort.pastillapp.databinding.FragmentHistoricalReminderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoricalReminderFragment : Fragment() {



    private  val viewModel: HistoricalReminderViewModel by viewModels()
    private lateinit var reminderAdapter: ReminderAdapter
    private val binding get() = _binding!!
    private var _binding: FragmentHistoricalReminderBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reminderAdapter = ReminderAdapter(mutableListOf(), findNavController(), this)

        viewModel.onCreate()





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoricalReminderBinding.inflate(inflater, container, false)
        val root: View = binding.root




        viewModel.remindersList.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                reminderAdapter.updateData(it)
            }
        })
        initRecycleView()


        binding.serachView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false // no lo necesitamos pero debemos implementarlo
            }

            override fun onQueryTextChange(newText: String?): Boolean {
              filterText(newText)
                return true
            }

        })

        binding.cbActive.setOnCheckedChangeListener{ _, isChecked ->
          viewModel.updateState(isChecked)
            if (isChecked){
                reminderAdapter.onlyActives()
            } else {
               viewModel.resetOriginalList()
            }

        }

        binding.btnHistoricalBack.setOnClickListener{
            findNavController().popBackStack()
        }

        return root
    }

    fun initRecycleView(){
        val recycleView = binding.rvHistoricalReminder

        recycleView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = reminderAdapter

    }


//    private fun filterText(query: String?){
//        reminderAdapter.filter(query ?: "")
//    }

    private fun filterText(query: String?) {
        if (query.isNullOrBlank()) {
            viewModel.resetOriginalList()
            Log.d("original", "en el filter text llamo al reset")

            return
        }
            reminderAdapter.filter(query)

    }


    fun deleteReminder(reminderId : Int) {
        val service = ActivityServiceApiBuilder.createReminder()
        val call = service.deleteReminderId(reminderId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    viewModel.onCreate()

                } else {
                    // Manejar la respuesta de error
                    val errorBody = response.errorBody()?.string()
                    Log.e("put33", "Respuesta fallo en el else: $errorBody")
                    Toast.makeText(context, "Hubo un error al eliminar el Recordatorio, intentelo mas tarde", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar el error de la solicitud
                Log.e("put33", "respuesta fallo en el failure")
            }
        })
    }
    }
