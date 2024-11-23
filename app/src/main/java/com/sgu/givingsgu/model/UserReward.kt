package com.sgu.givingsgu.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class UserReward(
    val id: Long,
    val userId: Long,
    val reward: Reward,
    val redeemDate: Date,
    val status: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readParcelable(Reward::class.java.classLoader)!!,
        parcel.readSerializable() as Date,
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(userId)
        parcel.writeParcelable(reward, flags)
        parcel.writeSerializable(redeemDate)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserReward> {
        override fun createFromParcel(parcel: Parcel): UserReward {
            return UserReward(parcel)
        }

        override fun newArray(size: Int): Array<UserReward?> {
            return arrayOfNulls(size)
        }
    }
}
