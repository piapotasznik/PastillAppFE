package edu.ort.pastillapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.ViewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), OnClickNavigate {

    private val homeViewModel: HomeViewModel by viewModels()
    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null
    private var reminderList: MutableList<ReminderLogToday> = mutableListOf()
    private var backFromFragment = false
    private lateinit var adapter: TodayReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executeFunctions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        homeViewModel.remidersLogs.observe(viewLifecycleOwner, Observer { reminders ->
//            reminders?.let {
//            reminderList= reminders
//                initRecylcerView()
//                Log.e("reminderList", "en el homoViewModel.remidnerlogs")
//            }
//        })

//        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer {
//            binding.loading.isVisible = it
//        })

        val emergencyContactBtn = binding.emergencyContactButton
        emergencyContactBtn.setOnClickListener {
            contactEmergencyUser()
        }

        initRecyclerView()
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


    override fun onPause() {
        super.onPause()
        backFromFragment = true
    }

    override fun onResume() {
        super.onResume()
        if (backFromFragment) {
            getTodayReminders()
            Log.e("backFromFragment", "esto es true")
            backFromFragment = false
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

        adapter = TodayReminderAdapter(reminderList, findNavController())
        binding.medList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.medList.adapter = adapter
    }

    override fun OnClickNavigate(reminder: Reminder) {
        val action =
            HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminder.reminderId)
        Log.e("click", "estoy clickeando aqui!!!")
        this.findNavController().navigate(action)
    }

    fun getTodayReminders() {
        Log.e("remindersLogs", "ejecuto el get today reminders")
        val service = ActivityServiceApiBuilder.createReminderLogService()
        var id = SharedPref.read(SharedPref.ID, UserSingleton.userId!!)
        service.getTodayReminders(id).enqueue(object :
            Callback<List<ReminderLogToday>> {
            override fun onResponse(
                call: Call<List<ReminderLogToday>>,
                response: Response<List<ReminderLogToday>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()
                    if (responseReminders != null) {
                        //  remidersLogs.postValue(responseReminders!!) // Actualiza el valor de remindersLogs
                        adapter.updateData(responseReminders)

                        Log.e("remindersLogs", "la respuesta esl ${responseReminders}")
                    }
                } else {
                    Log.e("chau", "respuesta no exitosa")
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                }
            }

            override fun onFailure(call: Call<List<ReminderLogToday>>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })
    }

    private fun executeFunctions() {
        CoroutineScope(Dispatchers.Main).launch {
            saveUserId() // Espera a que saveUserId() termine antes de continuar
        }
    }

    private fun saveUserId() {
        var email = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail)
        if (email != null) {
            val service = ActivityServiceApiBuilder.create()
            service?.getUserEmail(email)?.enqueue(object : Callback<ApiUserResponse> {
                override fun onResponse(
                    call: Call<ApiUserResponse>,
                    response: Response<ApiUserResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var userCreatedInformation = response.body()
                            UserSingleton.userId = userCreatedInformation?.userId
                            SharedPref.write(SharedPref.ID, userCreatedInformation?.userId)
                            Log.e("tarde pero seguro", "el user id es ${UserSingleton.userId}")
                            getTodayReminders()
                        }
                    }
                }

                override fun onFailure(call: Call<ApiUserResponse>, t: Throwable) {
                    // Manejar errores de red o solicitud
                    Log.e("error al taer id", "error al taer id user")
                }
            })
        }
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
}



