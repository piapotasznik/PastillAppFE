package edu.ort.pastillapp.Models

data class EmergencyContactRequest(
    val senderEmail: String,
    val recipientEmail: String,
    val status: String
)
