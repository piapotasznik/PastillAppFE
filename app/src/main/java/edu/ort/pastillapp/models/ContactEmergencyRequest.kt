package edu.ort.pastillapp.models
import com.google.gson.annotations.SerializedName

data class ContactEmergencyRequest(
    @SerializedName("userMail") val userMail: String,
    @SerializedName("contactEmergencyMail") val contactEmergencyMail: String
)