package edu.ort.pastillapp.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogsCalendarViewModel : ViewModel() {

    val logsCalendar = MutableLiveData<List<ReminderLogToday>?>()
    private val loading = MutableLiveData<Boolean>()


    fun getLogs(date:String) {
        loading.postValue(true)
        viewModelScope.launch {
             getLogsFrom(date)
            loading.postValue(false)
        }
    }

    private fun getLogsFrom(dateFrom: String) {
        val service = ActivityServiceApiBuilder.createReminderLogService()
        var id = SharedPref.read(SharedPref.ID, UserSingleton.userId!!)

        service.getLogsFrom(id,dateFrom).enqueue(object :
            Callback<List<ReminderLogToday>> {
            override fun onResponse(
                call: Call<List<ReminderLogToday>>,
                response: Response<List<ReminderLogToday>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()
                    if (responseReminders != null) {
                        logsCalendar.postValue(responseReminders)
                        Log.d("respuesta", " esta es la respuesta ${responseReminders}")

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
}

