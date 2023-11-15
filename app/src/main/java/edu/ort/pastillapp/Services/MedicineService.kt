package edu.ort.pastillapp.Services

import retrofit2.Call
import edu.ort.pastillapp.Models.Medicine
import retrofit2.http.GET

interface MedicineService {
    @GET("api/Medicine")
    fun getAllMedicines(): Call<List<Medicine>>
}