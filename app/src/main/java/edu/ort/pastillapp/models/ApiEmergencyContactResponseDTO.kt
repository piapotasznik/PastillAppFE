package edu.ort.pastillapp.models

data class ApiEmergencyContactResponseDTO(
    val userMail: String,
    val emergencyRequestId: Int,
    val accept: Boolean
)
