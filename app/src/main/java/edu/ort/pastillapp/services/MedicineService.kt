package edu.ort.pastillapp.services

import edu.ort.pastillapp.models.ServerResponse
import retrofit2.Call
import retrofit2.http.Body
import edu.ort.pastillapp.models.ApiReminderResponse
import edu.ort.pastillapp.models.Medicine
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface MedicineService {
    @GET("api/Medicine")
    fun getAllMedicines(): Call<List<Medicine>>
}