package edu.ort.pastillapp.models
data class ApiResponse(
    val userId: Integer?,
    val name: String,
    val lastName: String,
    val email: String,
    val emergencyUserId: Integer?,
    val emergencyUser: User?,

)

