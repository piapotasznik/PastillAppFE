package edu.ort.pastillapp.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ApiUserResponse
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel : ViewModel() {

    val remidersLogs = MutableLiveData<List<ReminderLogToday>>()
    val isLoading = MutableLiveData<Boolean>()


    fun onCreate() {

        viewModelScope.launch {
            try {
                saveUserId()
                getTodayReminders()

            } catch (e: Exception) {
                // Manejar cualquier excepción que pueda ocurrir durante executeFunctions
            } finally {

            }
        }
    }

    fun refreshData(){
        viewModelScope.launch {
            getTodayReminders()
        }
    }

    fun getTodayReminders() {
        Log.d("tarde ", "llamo al getTodayReminders")
        Log.e("tarde pero seguro", SharedPref.read(SharedPref.ID,UserSingleton.userId!!).toString() )
        Log.e("tarde pero seguro", UserSingleton.userId!!.toString() )
        val service = ActivityServiceApiBuilder.createReminderLogService()

            Log.d("tarde ", "entro al getTodayReminders")
        service.getTodayReminders(UserSingleton.userId!!).enqueue(object :
            Callback<List<ReminderLogToday>> {
            override fun onResponse(call: Call<List<ReminderLogToday>>, response: Response<List<ReminderLogToday>>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()

                    if (responseReminders != null) {
                        remidersLogs.postValue(responseReminders!!) // Actualiza el valor de remindersLogs
                        Log.e("remindersLogs", "la respuesta esl ${remidersLogs.value.toString()}")
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

    private fun saveUserId() {
        var email = SharedPref.read(SharedPref.EMAIL, UserSingleton.currentUserEmail.toString())

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
                            SharedPref.write(SharedPref.ID, userCreatedInformation?.userId)
                            Log.e("tarde pero seguro", SharedPref.read(SharedPref.ID,UserSingleton.userId!!).toString() )
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
        }
    }






}