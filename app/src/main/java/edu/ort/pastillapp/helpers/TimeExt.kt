package edu.ort.pastillapp.helpers

import android.widget.TextView
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

fun TextView.getTime(): LocalTime {
    return LocalTime.parse(text, formatter)
}

fun TextView.setTime(time: LocalTime) {
    text = time.toTimeText()
}

fun LocalTime.toTimeText(): String {
    return format(formatter)
}