package edu.ort.pastillapp.Services

import edu.ort.pastillapp.Models.ApiContactEmergencyServerResponse
import edu.ort.pastillapp.Models.Reminder
import edu.ort.pastillapp.Models.ReminderResponseById
import edu.ort.pastillapp.Models.ReminderUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReminderService {
    // profe fun getUser(): Call<ApiResponse<User>>
    @GET("api/Reminder/{reminderId}")
    fun getReminderId(@Path("reminderId") userId: Int): Call<Reminder>
    @GET("api/Reminder/{id}/reminder")
    fun getUserReminder(@Path("id") id: Int): Call<ReminderResponseById>
    @GET("api/Reminder")
    fun getReminders(): Call<List<Reminder>>
    @DELETE("api/Reminder/{reminderId}")
    fun deleteReminderId(@Path("reminderId") userId: Int): Call<Void>
    @PUT("api/Reminder/{reminderId}")
    fun putReminderId(@Path("reminderId") reminderId: Int, @Body body:ReminderUpdate): Call<ApiContactEmergencyServerResponse>
    @POST("api/Reminder")
    fun createReminder(@Body reminder: Reminder): Call<Void>
}