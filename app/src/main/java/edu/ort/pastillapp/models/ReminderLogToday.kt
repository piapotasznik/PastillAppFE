package edu.ort.pastillapp.models

data class ReminderLogToday(
    val reminderLogId: Int,
    val reminderId: Int,
    val dateTime: String,
    val taken: Boolean,
    val medicineId: Int,
    val name: String?,
    val dosage: Int,
    val presentation: String?,
    val observation: String?
)