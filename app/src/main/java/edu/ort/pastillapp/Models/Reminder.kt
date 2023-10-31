package edu.ort.pastillapp.Models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Reminder (
    val reminderId: Int,
    val userId: Int,
    var medicineId: Int,
    var quantity: Int,
    var presentation: String?,
    var dateTimeStart: String?, // Cambiado a Date
    var frequencyNumber: Int,
    var frequencyText: String?,
    var emergencyAlert: Boolean,
    var observation: String?,
    var intakeTimeNumber: Int,
    var intakeTimeText: String?,
    var endDateTime: String?, // Cambiado a Date
    var user:String?,
    var medicineName:String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(), // Cambiado a readDate
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(), // Cambiado a readDate
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(reminderId)
        parcel.writeInt(userId)
        parcel.writeInt(medicineId)
        parcel.writeInt(quantity)
        parcel.writeString(presentation)
        parcel.writeString(dateTimeStart)  // Cambiado a writeDate
        parcel.writeInt(frequencyNumber)
        parcel.writeString(frequencyText)
        parcel.writeByte(if (emergencyAlert) 1 else 0)
        parcel.writeString(observation)
        parcel.writeInt(intakeTimeNumber)
        parcel.writeString(intakeTimeText)
        parcel.writeString(endDateTime)// Cambiado a writeDate
        parcel.writeString(user)
        parcel.writeString(medicineName)
    }



    fun Parcel.readDate(): Date? {
        val time = this.readLong()
        return if (time != -1L) Date(time) else null
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reminder> {
        override fun createFromParcel(parcel: Parcel): Reminder {
            return Reminder(parcel)
        }

        override fun newArray(size: Int): Array<Reminder?> {
            return arrayOfNulls(size)
        }
    }
}
