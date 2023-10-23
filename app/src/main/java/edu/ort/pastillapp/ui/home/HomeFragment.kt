package edu.ort.pastillapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ort.pastillapp.UserSingleton
import edu.ort.pastillapp.databinding.FragmentHomeBinding
import edu.ort.pastillapp.helpers.Helpers
import edu.ort.pastillapp.listener.OnClickNavigate
import edu.ort.pastillapp.models.ApiUserResponse
import edu.ort.pastillapp.models.Reminder
import edu.ort.pastillapp.models.ReminderLogToday
import edu.ort.pastillapp.services.ActivityServiceApiBuilder
import edu.ort.pastillapp.ui.home.Adapter.DateAdapter
import edu.ort.pastillapp.ui.home.Adapter.TodayReminderAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() , OnClickNavigate {

    private lateinit var adapter: TodayReminderAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var reminderList : List<ReminderLogToday>



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // initRecylcerView()
//        homeViewModel.onCreate()
        executeFunctions()


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        homeViewModel.remidersLogs.observe(viewLifecycleOwner, Observer { reminders ->
//            reminders?.let {
//            reminderList= reminders
//                initRecylcerView()
//                Log.e("reminderList", "en el homoViewModel.remidnerlogs")
//            }
//
//
//        })

//        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer {
//            binding.loading.isVisible = it
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if(UserSingleton.userId!=null){
           initRecylcerView()
        }
    }

    fun initRecylcerView(){
        val dateAdapter = DateAdapter(Helpers().getDayOfMoth())
        val posicionInicial = Helpers().dayToday()+2

        binding.rvDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDate.adapter = dateAdapter
        binding.rvDate.smoothScrollToPosition(posicionInicial)



        adapter = TodayReminderAdapter(reminderList, findNavController())
        binding.medList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.medList.adapter = adapter
    }





    override fun OnClickNavigate(reminder :Reminder) {
        val action = HomeFragmentDirections.actionNavigationHomeToEditReminderFragment(reminder.reminderId)
        Log.e("click","estoy clickeando aqui!!!")
        this.findNavController().navigate(action)
    }

    fun getTodayReminders() {
        Log.e("remindersLogs", "ejecuto el get today reminders")
        val service = ActivityServiceApiBuilder.createReminderLogService()
        service.getTodayReminders(UserSingleton.userId!!).enqueue(object :
            Callback<List<ReminderLogToday>> {
            override fun onResponse(call: Call<List<ReminderLogToday>>, response: Response<List<ReminderLogToday>>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()

                    if (responseReminders != null) {
                      //  remidersLogs.postValue(responseReminders!!) // Actualiza el valor de remindersLogs
                        reminderList = responseReminders
                        initRecylcerView()
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


    fun executeFunctions() {
        CoroutineScope(Dispatchers.Main).launch {
            saveUserId() // Espera a que saveUserId() termine antes de continuar

        }
    }

    suspend fun saveUserId() {
        var email = UserSingleton.currentUserEmail
        if (email!=null && UserSingleton.userId == null) {
            val service = ActivityServiceApiBuilder.create()
            service?.getUserEmail(email)?.enqueue(object : Callback<ApiUserResponse> {
                override fun onResponse(
                    call: Call<ApiUserResponse>,
                    response: Response<ApiUserResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var userCreatedInformation = response.body()
                            UserSingleton.userId= userCreatedInformation?.userId
                            Log.e("tarde pero seguro","el user id es ${UserSingleton.userId}")
                            getTodayReminders()

                        }
                    }
                }

                override fun onFailure(call: Call<ApiUserResponse>, t: Throwable) {
                    // Manejar errores de red o solicitud
                    Log.e("error al taer id", "error al taer id user")
                }
            })
        } else {
            getTodayReminders()
        }
    }



}