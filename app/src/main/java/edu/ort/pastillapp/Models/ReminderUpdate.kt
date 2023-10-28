package edu.ort.pastillapp.Models

data class ReminderUpdate (
        val reminderId: Int,
        val quantity: Int,
        val presentation: String,
        val dateTimeStart: String,
        val frequencyNumber: Int,
        val frequencyText: String,
        val intakeTimeNumber: Int,
        val intakeTimeText: String,
        val emergencyAlert: Boolean,
        val keepPendingLogs: Boolean,
        val observation: String
    )

