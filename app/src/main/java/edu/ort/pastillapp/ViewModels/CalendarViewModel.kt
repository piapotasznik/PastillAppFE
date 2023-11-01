package edu.ort.pastillapp.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewModel : ViewModel() {

    val remindersList = MutableLiveData<List<String>?>()

    val logs = MutableLiveData<List<ReminderLogToday>?>()
    val loading = MutableLiveData<Boolean>()




    fun getLogs(date:String) {
        loading.postValue(true)
        viewModelScope.launch {
            getLogsFrom(date)
            logs.value = emptyList()
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
                        logs.postValue(responseReminders)
                        Log.d("respuesta", " esta es la respuesta ${responseReminders}")

                    } else {
                        logs.value = emptyList()
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