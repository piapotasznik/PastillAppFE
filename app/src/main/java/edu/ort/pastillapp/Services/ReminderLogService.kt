package edu.ort.pastillapp.Services

import edu.ort.pastillapp.Models.ReminderLog
import edu.ort.pastillapp.Models.ReminderLogToday
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReminderLogService {
    @GET("api/Reminder/{id}/reminderlogs")
    fun getReminderLogById(@Path("id") id: Int): Call<ReminderLog>

    @GET("api/Reminder/reminderLogsFromToday/{userId}")
    fun getTodayReminders(@Path("userId") id: Int): Call<List<ReminderLogToday>>

    @GET("/api/Reminder/reminders/logs/{userId}/{dateString}")
    fun getLogsFrom(
        @Path("userId") id: Int,
        @Path("dateString") dateString: String
    ): Call<List<ReminderLogToday>>

    @GET("api/Reminder/reminderlogs/{userId}/{dateStart}/{dateFinish}")
    fun getLogsFromUpto(
        @Path("userId") id: Int,
        @Path("dateStart") dateStart: String,
        @Path("dateFinish") dateFinish: String
    ): Call<List<ReminderLogToday>>

    @PUT("/api/Reminder/reminderlogs/{reminderLogId}/taken")
    fun reminderLogTaken(@Path("reminderLogId") reminderLogId: Int): Call<Void>
}