package edu.ort.pastillapp

import java.util.regex.Pattern

class ValidationEmail {
    companion object {
        fun validate(email: String): Boolean {
            val regex = Pattern.compile(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$",
                Pattern.CASE_INSENSITIVE
            )
            return regex.matcher(email).matches()
        }
    }
}
