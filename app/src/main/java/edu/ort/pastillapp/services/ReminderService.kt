package edu.ort.pastillapp.services

import edu.ort.pastillapp.models.ApiResponse
import edu.ort.pastillapp.models.Reminder
import edu.ort.pastillapp.models.ReminderUpdate
import edu.ort.pastillapp.models.ServerResponse
import edu.ort.pastillapp.models.User
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
    @GET("api/Reminder/{userId}/reminder")
    fun getUserReminder(@Path("userId") email: String): Call<List<Reminder>>
    @GET("api/Reminder")
    fun getReminders(): Call<List<Reminder>>
    @DELETE("api/Reminder/{reminderId}")
    fun deleteReminderId(@Path("reminderId") userId: Int): Call<Void>
    @PUT("api/Reminder/{reminderId}")
    fun putReminderId(@Path("reminderId") reminderId: Int, @Body body:ReminderUpdate): Call<ServerResponse>
    @POST("api/Reminder")
    fun createReminder(@Body reminder: Reminder): Call<Void>
}