package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.databinding.FragmentHomeBinding
import edu.ort.pastillapp.Helpers.Helpers
import edu.ort.pastillapp.Listeners.OnClickNavigate
import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import edu.ort.pastillapp.Adapters.DateAdapter
import edu.ort.pastillapp.Adapters.TodayReminderAdapter
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Listeners.OnCheckBoxClickListener
import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.ViewModels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), OnClickNavigate, OnCheckBoxClickListener {


    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: TodayReminderAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecyclerView()
        sharedViewModel.onCreate()
        sharedViewModel.remidersLogs.observe(viewLifecycleOwner, Observer { reminders ->
            reminders?.let {
           adapter.updateData(it)

            }
        })

        val emergencyContactBtn = binding.emergencyContactButton
        emergencyContactBtn.setOnClickListener {
            contactEmergencyUser()
        }


        return root
    }

    override fun onStart() {
        super.onStart()
        val btnReminderHistory = binding.btnReminderHistory

        btnReminderHistory.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToHistoricalReminderFragment()
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerView() {
        val dateAdapter = DateAdapter(Helpers().getDayOfMoth())
        val initialPosition = Helpers().dayToday() + 2

        binding.rvDate.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = dateAdapter
        binding.rvDate.smoothScrollToPosition(initialPosition)

        adapter = TodayReminderAdapter(mutableListOf(), findNavController())
        binding.medList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setOnCheckBoxClickListener(this)
        binding.medList.adapter = adapter
    }

    override fun OnClickNavigate(reminder: Reminder) {
        val action =
            HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminder.reminderId)
        Log.e("click", "estoy clickeando aqui!!!")
        this.findNavController().navigate(action)
    }


    private fun contactEmergencyUser() {
        val email = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail)
        if (email != null) {
            val service = ActivityServiceApiBuilder.create()
            service?.sendEmergencyMessage(email)
                ?.enqueue(object : Callback<ApiContactEmergencyServerResponse> {
                    override fun onResponse(
                        call: Call<ApiContactEmergencyServerResponse>,
                        response: Response<ApiContactEmergencyServerResponse>
                    ) {
                        if (response.isSuccessful) {
                            val message = "Solicitud de emergencia enviada"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        } else {
                            val message = "Error en la solicitud"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ApiContactEmergencyServerResponse>,
                        t: Throwable
                    ) {
                        // Manejar errores de red o solicitud
                        val message = "Error de comunicación"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    override fun onCheckBoxClicked(position: Int) {
      Log.d("Estoy Clickeando", "Click click!!! + ${position.toString()}")
        val service = ActivityServiceApiBuilder.createReminderLogService()

        service.reminderLogTaken(position).enqueue(object :
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()
                    if (responseReminders != null) {
                        //  remidersLogs.postValue(responseReminders!!) // Actualiza el valor de remindersLogs
                      sharedViewModel.refreshData()

                    }
                } else {
                    Log.e("chau", "respuesta no exitosa")
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })
    }

}



