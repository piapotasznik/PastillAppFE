package edu.ort.pastillapp.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Reminder (
    val userId: Int,
    var medicineId: Int,
    var quantity: String?,
    var presentation: String?,
    var dateTimeStart: Date?,
    var frequencyInterval: String?,
    var frequencyInt: Int,
    var emergencyAlert: Boolean,
    var observation: String?,
    var durationType: String?,
    var durationInt: Int,
    val user: String?,
    var medicine:String?,

    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        null,
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
        dateTimeStart = parcel.readDate()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeInt(medicineId)
        parcel.writeString(quantity)
        parcel.writeString(presentation)
        parcel.writeString(frecuencyInterval)
        parcel.writeInt(frequencyInt)
        parcel.writeByte(if (emergencyAlert) 1 else 0)
        parcel.writeString(observation)
        parcel.writeString(durationType)
        parcel.writeInt(durationInt)
        parcel.writeString(user)
        parcel.writeString(medicine)
    }
    fun Parcel.writeDate(date: Date?) {
        if (date != null) {
            this.writeLong(date.time)
        } else {
            this.writeLong(-1)
        }
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

