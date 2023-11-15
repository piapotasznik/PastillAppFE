package edu.ort.pastillapp.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.fido.u2f.api.messagebased.ResponseType
import edu.ort.pastillapp.Helpers.SharedPref
import edu.ort.pastillapp.Helpers.UserSingleton
import edu.ort.pastillapp.Models.DailyStatus
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
    val dailyStatus = MutableLiveData<DailyStatus?>()
    var responseReminders2 : Boolean = false
    val callSuccess: LiveData<Boolean> get() = _callSuccess
    private val _callSuccess = MutableLiveData<Boolean>()
    val callSuccessLogs: LiveData<Boolean> get() = _callSuccessLogs
    private val _callSuccessLogs = MutableLiveData<Boolean>()






    fun getLogs(date:String) {
        loading.postValue(true)
        viewModelScope.launch {
            getLogsFrom(date)
            logs.value = emptyList()
            loading.postValue(false)

        }
    }

    fun getDailyStatus(date:String) {
        viewModelScope.launch {
            getDailyStatusFrom(date)
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
                        responseReminders2 = (responseReminders.size == 0)
                        _callSuccessLogs.postValue(false)

                    } else {
                        logs.value = emptyList()
                        _callSuccessLogs.postValue(true)
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

    private fun getDailyStatusFrom(dateFrom: String) {
        val service = ActivityServiceApiBuilder.createDailyStatus()
        var id = SharedPref.read(SharedPref.ID, UserSingleton.userId!!)

        service.getLogsFrom(id,dateFrom).enqueue(object :
            Callback<DailyStatus> {
            override fun onResponse(
                call: Call<DailyStatus>,
                response: Response<DailyStatus>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val dailyStatusResponse = response.body()
                    if (dailyStatusResponse != null) {
                        dailyStatus.postValue(dailyStatusResponse)
                       _callSuccess.postValue(true)
                    } else {
                        _callSuccess.postValue(false)

                    }
                } else {
                    Log.e("chau", "respuesta no exitosa")
                    Log.e("Respuesta no exitosa", " esta es la respuesta $response")
                    _callSuccess.postValue(false)
                }
            }

            override fun onFailure(call: Call<DailyStatus>, t: Throwable) {
                Log.e("Example", t.stackTraceToString())
            }
        })

    }


}