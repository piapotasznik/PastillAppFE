package edu.ort.pastillapp.Helpers

import edu.ort.pastillapp.Models.Day
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Helpers() {

    fun dayToday(): Int {
        val actualDate = Date()
        val format = SimpleDateFormat("d")
        return format.format(actualDate).toInt()
    }

    fun getDayOfMoth(): List<Day> {
        val days = mutableListOf<Day>()
        val calendar = Calendar.getInstance()
        val numericDayFormat = SimpleDateFormat("d", Locale.getDefault())
        val symbols = DateFormatSymbols(Locale("es", "ES"))
        val dayNames = symbols.shortWeekdays // Obtiene los nombres cortos de los días en español
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val ultimoDiaDelMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Obtener los tres días anteriores a hoy
        for (i in today - 3 until today) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val dayOfWeek = dayNames[calendar.get(Calendar.DAY_OF_WEEK)]
            val firstLetter = dayOfWeek[0].toString().uppercase()
            val numberDay = numericDayFormat.format(calendar.time).toInt()
            val day = Day(firstLetter, numberDay, false)
            days.add(day)
        }

        // Obtener el día de hoy
        calendar.set(Calendar.DAY_OF_MONTH, today)
        val weekdayToday = dayNames[calendar.get(Calendar.DAY_OF_WEEK)]
        val firstLetterToday = weekdayToday[0].toString().uppercase()
        val dayToday = Day(firstLetterToday, today, true)
        days.add(dayToday)

        // Obtener los tres días posteriores a hoy
        for (i in today + 1..today + 3) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val weekday = dayNames[calendar.get(Calendar.DAY_OF_WEEK)]
            val weekdayFirstLetter = weekday[0].toString().uppercase()
            val weekdayNumberDay = numericDayFormat.format(calendar.time).toInt()
            val day = Day(weekdayFirstLetter, weekdayNumberDay, false)
            days.add(day)
        }
        return days
    }

    fun convertDate(dateString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date: Date = entryFormat.parse(dateString) ?: Date()
        return exitFormat.format(date)
    }

    fun convertDateSM(dateString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault())

        val date: Date = entryFormat.parse(dateString) ?: Date()
        return exitFormat.format(date)
    }

    fun convertOnlyDate(dateString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val exitFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val date: Date = entryFormat.parse(dateString) ?: Date()
        return exitFormat.format(date)
    }

    fun convertDateOnlyTime(dateString: String): String {
        val entryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val exitFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date: Date = entryFormat.parse(dateString) ?: Date()
        return exitFormat.format(date)
    }


    fun convertInvertDate(dateString: String): String {
        val entryFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val exitFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

        val date: Date = entryFormat.parse(dateString) ?: Date()
        return exitFormat.format(date)
    }

    fun dateHasAlreadyPassed(dateToCompare: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        return try {
            val date = format.parse(dateToCompare)
            val actualDate = Date()
            date != null && date.before(actualDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            false
        }
    }

    fun translateFrequency(englishText: String): String {
        return when (englishText) {
            "DAY" -> "Day"
            "HOURS" -> "Horas"
            "HOUR" -> "Horas"
            "MONTH" -> "Meses"
            "WEEK" -> "Semanas"
            "YEAR" -> "Años"
            else -> "no se pudo traducir" // Por si acaso no hay una traducción definida
        }
    }

    fun translateFrequencyEn(englishText: String): String {
        return when (englishText) {
            "Dias" -> "DAY"
            "Horas" -> "HOUR"
            "Meses" -> "MONTH"
            "Semanas" -> "WEEK"
            "Años" -> "YEAR"
            else -> "no se pudo traducir" // Por si acaso no hay una traducción definida
        }
    }
}