package edu.ort.pastillapp.helpers

import edu.ort.pastillapp.ui.home.Dia
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Helpers(){

fun dayToday (): Int{
    val fechaActual = Date()

    val formato = SimpleDateFormat("d")
    val diaNumerico = formato.format(fechaActual).toInt()
    return diaNumerico
}

    fun getDayOfMoth(): List<Dia> {
        val dias = mutableListOf<Dia>()
        val calendario = Calendar.getInstance()
        val formatoNumeroDia = SimpleDateFormat("d", Locale.getDefault())

        val symbols = DateFormatSymbols(Locale("es", "ES"))
        val nombresDias = symbols.shortWeekdays // Obtiene los nombres cortos de los días en español

        val today = calendario.get(Calendar.DAY_OF_MONTH)
        val ultimoDiaDelMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Obtener los tres días anteriores a hoy
        for (i in today - 3 until today) {
            calendario.set(Calendar.DAY_OF_MONTH, i)
            val diaSemana = nombresDias[calendario.get(Calendar.DAY_OF_WEEK)]
            val primeraLetra = diaSemana[0].toString().uppercase()
            val numeroDia = formatoNumeroDia.format(calendario.time).toInt()
            val dia = Dia(primeraLetra, numeroDia, false)
            dias.add(dia)
        }

        // Obtener el día de hoy
        calendario.set(Calendar.DAY_OF_MONTH, today)
        val diaSemanaHoy = nombresDias[calendario.get(Calendar.DAY_OF_WEEK)]
        val primeraLetraHoy = diaSemanaHoy[0].toString().uppercase()
        val diaHoy = Dia(primeraLetraHoy, today, true)
        dias.add(diaHoy)

        // Obtener los tres días posteriores a hoy
        for (i in today + 1..today + 3) {
            calendario.set(Calendar.DAY_OF_MONTH, i)
            val diaSemana = nombresDias[calendario.get(Calendar.DAY_OF_WEEK)]
            val primeraLetra = diaSemana[0].toString().uppercase()
            val numeroDia = formatoNumeroDia.format(calendario.time).toInt()
            val dia = Dia(primeraLetra, numeroDia, false)
            dias.add(dia)
        }

        return dias
    }

    fun crearDateTimeManualmente(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)

        return calendar.time
    }

    fun dateToString(date: Date?): String {
        if (date == null) return ""

        val formato = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        return formato.format(date)
    }

    fun stringToDate(dateString: String): Date? {
        val format = "dd-MM-yyyy HH:mm"
        val sdf = SimpleDateFormat(format, Locale.getDefault())

        return try {
            sdf.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}