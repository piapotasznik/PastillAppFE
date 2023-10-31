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

    val remindersList = MutableLiveData<List<Reminder>?>()
    val originalReminderList = MutableLiveData<List<Reminder>>()
    val onlyActive = MutableLiveData<Boolean>()

    fun onCreate() {
        onlyActive.postValue(false)

        viewModelScope.launch {
             getAllReminders { responseReminders ->
                 remindersList.postValue(responseReminders)
                 originalReminderList.postValue(responseReminders)

             }
        }
    }

    fun resetOriginalList() {
        val originalReminders = originalReminderList.value
        Log.d("original", "la lista esta ${originalReminders.toString()}")
        if (!originalReminders.isNullOrEmpty()){
            remindersList.postValue(originalReminders)
            Log.d("original", "paso el null or empty ${originalReminders.toString()}")

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
    fun updateState(state : Boolean){
        onlyActive.postValue(state)
        if (!state){
            resetOriginalList()

        }
    }


}