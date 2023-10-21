package edu.ort.pastillapp

data class EmergencyContactRequest(
    val senderEmail: String,
    val recipientEmail: String,
    val status: String
)
