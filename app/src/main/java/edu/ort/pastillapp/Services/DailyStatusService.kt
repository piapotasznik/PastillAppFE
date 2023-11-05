package edu.ort.pastillapp.Services

import edu.ort.pastillapp.Models.DailyStatus
import edu.ort.pastillapp.Models.ReminderLogToday
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DailyStatusService {

    @GET("/api/DailyStatus/dailystatus/{userId}/{dateString}")
    fun getLogsFrom(
        @Path("userId") id: Int,
        @Path("dateString") dateString: String
    ): Call<DailyStatus>

}