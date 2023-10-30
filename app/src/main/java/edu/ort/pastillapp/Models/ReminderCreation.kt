package edu.ort.pastillapp.Models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class ReminderCreation (
    val userId: Int,
    var medicineId: Int,
    var quantity: Int,
    var presentation: String?,
    var dateTimeStart: String?, // Cambiado a Date
    var frequencyType: String?,
    var frequencyValue: Int,
    var durationType: String?,
    var durationValue: Int,
    var emergencyAlert: Boolean,
    var observation: String?,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(), // Cambiado a readDate
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeInt(medicineId)
        parcel.writeInt(quantity)
        parcel.writeString(presentation)
        parcel.writeString(dateTimeStart)  // Cambiado a writeDate
        parcel.writeString(frequencyType)
        parcel.writeInt(frequencyValue)
        parcel.writeString(durationType)
        parcel.writeInt(durationValue)
        parcel.writeByte(if (emergencyAlert) 1 else 0)
        parcel.writeString(observation)
    }


    fun Parcel.readDate(): Date? {
        val time = this.readLong()
        return if (time != -1L) Date(time) else null
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReminderCreation> {
        override fun createFromParcel(parcel: Parcel): ReminderCreation {
            return ReminderCreation(parcel)
        }

        override fun newArray(size: Int): Array<ReminderCreation?> {
            return arrayOfNulls(size)
        }
    }
}
