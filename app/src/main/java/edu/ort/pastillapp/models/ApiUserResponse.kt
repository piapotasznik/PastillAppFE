package edu.ort.pastillapp.models
data class ApiUserResponse(
    val userId: Int?,
    val name: String,
    val lastName: String,
    val email: String,
    val emergencyUserId: Int?,
    val emergencyUser: User?,

)

