package edu.ort.pastillapp.services

import edu.ort.pastillapp.models.ReminderLog
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReminderLogService {
    @GET("api/Reminder/{id}/reminderlogs")
    fun getReminderLogById(@Path("id") id: Int): Call<ReminderLog>
}