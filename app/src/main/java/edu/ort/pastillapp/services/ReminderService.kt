package edu.ort.pastillapp.services

import edu.ort.pastillapp.models.ApiReminderResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReminderService {

    @GET("api/Reminder/{reminderId}")
    fun getReminder(@Path("reminderId") reminderId: Int): Call<ApiReminderResponse>

    @GET("api/Reminder")
    fun getAllReminders(): Call<List<ApiReminderResponse>>

    @GET("api/User/{userId}/reminder")
    fun getReminderByUserId(@Path("userId") userId: Int): Call<List<ApiReminderResponse>>

    @POST("api/Reminder")
    //fun createReminder(@Body createReminderDTO: CreateReminderDTO): Call<Void>

    @PUT("api/Reminder/{reminderId}")
    //fun updateReminder(@Path("reminderId") reminderId: Int, @Body reminder: Reminder): Call<Void>

    @DELETE("api/Reminder/{reminderId}")
    fun deleteReminder(@Path("reminderId") reminderId: Int): Call<Void>
}