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



//    fun getRange(from:String, upto : String) {
//        viewModelScope.launch {
//             searchReminderLogsRange(from, upto)
//
//        }
//    }




//    private fun searchReminderLogsRange(dateFrom: String, dateUpto: String) {
//        val service = ActivityServiceApiBuilder.createReminderLogService()
//        var id = SharedPref.read(SharedPref.ID, UserSingleton.userId!!)
//
//        service.getLogsFromUpto(id,dateFrom,dateUpto).enqueue(object :
//            Callback<List<ReminderLogToday>> {
//            override fun onResponse(
//                call: Call<List<ReminderLogToday>>,
//                response: Response<List<ReminderLogToday>>
//            ) {
//                if (response.isSuccessful && response.body() != null) {
//                    val responseReminders = response.body()
//                    if (responseReminders != null) {
//                        //  remidersLogs.postValue(responseReminders!!) // Actualiza el valor de remindersLogs
//
//                            remindersList.postValue(responseReminders)
//
//
//                        Log.e("fecha", "la respuesta esl ${responseReminders}")
//                    }
//                } else {
//                    Log.e("chau", "respuesta no exitosa")
//                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
//                }
//            }
//
//            override fun onFailure(call: Call<List<ReminderLogToday>>, t: Throwable) {
//                Log.e("Example", t.stackTraceToString())
//            }
//        })
//
//    }
}