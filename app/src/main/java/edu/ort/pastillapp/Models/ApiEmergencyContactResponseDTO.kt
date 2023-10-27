package edu.ort.pastillapp.Models;

data class ApiEmergencyContactResponseDTO(
    val userMail: String,
    val emergencyRequestId: Int,
    val accept: Boolean
)
