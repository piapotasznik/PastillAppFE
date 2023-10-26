package edu.ort.pastillapp.Models;
import com.google.gson.annotations.SerializedName

data class ApiContactEmergencyRequest(
    @SerializedName("userMail") val userMail: String,
    @SerializedName("contactEmergencyMail") val contactEmergencyMail: String
)