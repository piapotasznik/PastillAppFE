package edu.ort.pastillapp.models

import java.util.Date

data class Reminder (
    val userId: Int,
    val medicineId: Int,
    val quantity: String,
    val dateTimeStart: String,
    val frequency: Int,
    val emergencyAlert: Boolean,
    val observation: String,
    val finalDate: String,
    val user: String,
    val medicine:String,

)