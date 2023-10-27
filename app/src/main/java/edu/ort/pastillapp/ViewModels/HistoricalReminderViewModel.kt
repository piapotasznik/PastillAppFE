package edu.ort.pastillapp.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderLogToday
import edu.ort.pastillapp.Models.ReminderResponseById
import edu.ort.pastillapp.Services.ActivityServiceApiBuilder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoricalReminderViewModel : ViewModel() {

    val remindersList = MutableLiveData<List<Reminder>>()



    fun onCreate() {
        //Aca creo ese objecto

        viewModelScope.launch {
             getAllReminders { responseReminders ->
                 remindersList.postValue(responseReminders)
                 Log.d("que pasa", responseReminders.toString())
             }
        }
    }


    private fun getAllReminders(callback: (List<Reminder>) -> Unit) {
        val service = ActivityServiceApiBuilder.createReminder()
        var id = SharedPref.read(SharedPref.ID, UserSingleton.userId!!)

        service.getUserReminder(id).enqueue(object : Callback<ReminderResponseById> {
            override fun onResponse(
                call: Call<ReminderResponseById>,
                response: Response<ReminderResponseById>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val responseReminders = response.body()?.remindersByUserId
                    if (responseReminders != null) {
                        callback(responseReminders)
                    }
                } else {
                    Log.e("chau", "respuesta no exitosa")
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                }
            }

            override fun onFailure(call: Call<ReminderResponseById>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })
    }


}