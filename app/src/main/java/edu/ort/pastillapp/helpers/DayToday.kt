package edu.ort.pastillapp.helpers

import java.text.SimpleDateFormat
import java.util.Date

class DayToday {
    val fechaActual = Date()

    val formato = SimpleDateFormat("d")
    val diaNumerico = formato.format(fechaActual).toInt()
    fun dayToday(): Int {
        return diaNumerico
    }



}