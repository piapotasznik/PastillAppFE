package edu.ort.pastillapp.Services

import edu.ort.pastillapp.Models.DailyStatus
import edu.ort.pastillapp.Models.DailyStatusDTO
import edu.ort.pastillapp.Models.UpdateDailyStatusDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DailyStatusService {

    @GET("/api/DailyStatus/dailystatus/{userId}/{dateString}")
    fun getLogsFrom(
        @Path("userId") id: Int,
        @Path("dateString") dateString: String
    ): Call<DailyStatus>


    @POST("api/DailyStatus")
    fun createDailyStatus(@Body dailyStatusDTO: DailyStatusDTO): Call<Void>

    @PUT("api/DailyStatus/updateStatus/{userId}")
    fun updateStatus(
        @Path("userId") userId: Int,
        @Body updateDailyStatusDTO: UpdateDailyStatusDTO
    ): Call<Void>
}