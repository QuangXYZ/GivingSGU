package com.sgu.givingsgu.model

import android.os.Parcel
import android.os.Parcelable

data class Reward(
    val id: Long,

    val name: String,

    val pointsRequired: Int,

    val quantity: Int,

    val imageUrl: String,

    val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(pointsRequired)
        parcel.writeInt(quantity)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reward> {
        override fun createFromParcel(parcel: Parcel): Reward {
            return Reward(parcel)
        }

        override fun newArray(size: Int): Array<Reward?> {
            return arrayOfNulls(size)
        }
    }
}