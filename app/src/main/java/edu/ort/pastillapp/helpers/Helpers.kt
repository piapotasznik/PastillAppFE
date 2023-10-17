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
    fun convertirFecha(fechaString: String): String {
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        val fecha: Date = formatoEntrada.parse(fechaString) ?: Date()
        return formatoSalida.format(fecha)
    }


    fun convertirFechaInversa(fechaString: String): String {
        val formatoEntrada = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

        val fecha: Date = formatoEntrada.parse(fechaString) ?: Date()
        return formatoSalida.format(fecha)
    }

    fun translateFrequency(englishText: String): String {
        return when (englishText) {
            "DAY" -> "Dias"
            "HOURS" -> "Horas"
            "MONTH" -> "Meses"
            "WEEK" -> "Semanas"
            "YEAR" -> "Años"
            else -> "no se pudo traducir" // Por si acaso no hay una traducción definida
        }
    }

    fun translateFrequencyEn(englishText: String): String {
        return when (englishText) {
            "Dias" -> "DAY"
            "Horas" -> "HOURS"
            "Meses" -> "MONTH"
            "Semanas" -> "WEEK"
            "Años" -> "YEAR"
            else -> "no se pudo traducir" // Por si acaso no hay una traducción definida
        }
    }


}