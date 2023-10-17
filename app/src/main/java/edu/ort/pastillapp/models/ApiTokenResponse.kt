package edu.ort.pastillapp.models

data class ApiTokenResponse(

    val tokenId: Int,
    val deviceToken: String,
    val userEmail: String,
    val userId: Int,
    val user: User?,

)
