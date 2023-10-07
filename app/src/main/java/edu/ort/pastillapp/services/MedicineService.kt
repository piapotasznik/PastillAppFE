package edu.ort.pastillapp.services

import edu.ort.pastillapp.models.Medicine
import edu.ort.pastillapp.models.Reminder
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MedicineService {

    @GET("api/Medicine/{medicineId}")
    fun getMedicine(@Path("medicineId") medicineId: Int): Call<Medicine>

    @GET("api/Medicine/")
    fun getAll(): Call<List<Medicine>>
}

