package edu.ort.pastillapp.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Reminder (
    val userId: Int,
    val medicineId: Int,
    val quantity: String?,
    val presentation: String?,
    val dateTimeStart: String?,
    val frecuencyInterval: String?,
    val frequencyInt: Int,
    val emergencyAlert: Boolean,
    val observation: String?,
    val durationType: String?,
    val durationInt: Int,
    val user: String?,
    val medicine:String?,

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeInt(medicineId)
        parcel.writeString(quantity)
        parcel.writeString(presentation)
        parcel.writeString(dateTimeStart)
        parcel.writeString(frecuencyInterval)
        parcel.writeInt(frequencyInt)
        parcel.writeByte(if (emergencyAlert) 1 else 0)
        parcel.writeString(observation)
        parcel.writeString(durationType)
        parcel.writeInt(durationInt)
        parcel.writeString(user)
        parcel.writeString(medicine)
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