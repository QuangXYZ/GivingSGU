package com.sgu.givingsgu.model


import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Project(
    val projectId: Long,
    val name: String,
    val description: String? = null,
    val startDate: Date,
    val endDate: Date,
    val targetAmount: Double,
    val currentAmount: Double? = null,
    val status: String? = null,
    val numberDonors: Int = 0,
    val imageUrls: String? = null,
    val managedBy: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readSerializable() as Date,
        parcel.readSerializable() as Date,
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(projectId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeSerializable(startDate)
        parcel.writeSerializable(endDate)
        parcel.writeDouble(targetAmount)
        parcel.writeValue(currentAmount)
        parcel.writeString(status)
        parcel.writeInt(numberDonors)
        parcel.writeString(imageUrls)
        parcel.writeLong(managedBy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Project> {
        override fun createFromParcel(parcel: Parcel): Project {
            return Project(parcel)
        }

        override fun newArray(size: Int): Array<Project?> {
            return arrayOfNulls(size)
        }
    }
}