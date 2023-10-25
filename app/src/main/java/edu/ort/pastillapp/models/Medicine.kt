package edu.ort.pastillapp.models

data class Medicine (
    val medicineId: Int,
    val name: String,
    val dosage: String,
    val presentation: String,
) {
    override fun toString(): String {
        return name
    }
}
