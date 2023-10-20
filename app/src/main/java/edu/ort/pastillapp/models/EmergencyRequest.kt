package edu.ort.pastillapp.models

data class EmergencyRequest(
    val userMail: String,
    val emergencyRequestId: Int,
    val accept: Boolean
)
